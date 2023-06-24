package com.nmihalek.dogs.feature.breeddetails.presentation

import androidx.lifecycle.SavedStateHandle
import com.nmihalek.dogs.MainCoroutineRule
import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeddetails.domain.usecase.GetBreedsPicturesUseCase
import com.nmihalek.dogs.feature.breeddetails.domain.usecase.RefreshBreedsPicturesUseCase
import com.nmihalek.dogs.feature.breeddetails.presentation.model.toBreedPictureUiItem
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import com.nmihalek.dogs.feature.breeds.domain.usecase.RefreshBreedsUseCase
import com.nmihalek.dogs.feature.common.presentation.navigation.DogsDestinations
import com.nmihalek.dogs.feature.favourites.domain.usecase.UpdateFavouriteUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BreedDetailsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    lateinit var getBreedsPicturesUseCase: GetBreedsPicturesUseCase
    @RelaxedMockK
    lateinit var refreshBreedsUseCase: RefreshBreedsPicturesUseCase
    @RelaxedMockK
    lateinit var updateFavouriteUseCase: UpdateFavouriteUseCase
    @RelaxedMockK
    lateinit var savedStateHandle: SavedStateHandle

    lateinit var sut: BreedDetailsViewModel

    lateinit var breedsPicturesFlow: MutableSharedFlow<List<Picture>>
    val akitaPicture = Picture(breed = "akita", imageUrl = "https://image/akita.jpg")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        breedsPicturesFlow = MutableSharedFlow(replay = 1)
        every { savedStateHandle.get<String>(DogsDestinations.ARGUMENT_BREED_NAME) } returns "akita"
        every { savedStateHandle.get<String>(DogsDestinations.ARGUMENT_SUB_BREED_NAME) } returns null
        coEvery { getBreedsPicturesUseCase.invoke(any()) } returns breedsPicturesFlow
        sut = BreedDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getBreedsPicturesUseCase = getBreedsPicturesUseCase,
            refreshBreedsUseCase = refreshBreedsUseCase,
            updateFavouriteUseCase = updateFavouriteUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `when init, should set refreshing to true`() {
        assertTrue(sut.refreshing)
    }

    @Test
    fun `given existing pictures, when pictures received, should set refreshing false and map picture to ui item`() {
        assertTrue(sut.refreshing)

        breedsPicturesFlow.tryEmit(listOf(akitaPicture))

        val result = runBlocking { sut.pictures.first() }
        assertFalse(sut.refreshing)
        assertEquals(listOf(akitaPicture.toBreedPictureUiItem()), result)
    }
    //error running due to https://github.com/mockk/mockk/issues/485
    //same for onFavouriteClicked. most likely needs a dispatcher manually being passed
    //@Test
    fun `given refresh success, when refreshing pictures, should set refreshing to false`() = runTest {
        assertTrue(sut.refreshing)
        val mockValue: Result<List<Picture>> = Result.success(listOf(akitaPicture))
        coEvery { refreshBreedsUseCase.invoke(any<Breed>()) } returns mockValue

        sut.refreshBreedPictures()

        assertFalse(sut.refreshing)
        coVerify { refreshBreedsUseCase.invoke(any()) }
        confirmVerified(refreshBreedsUseCase)
    }

    @Test
    fun `given refresh fail, when refreshing pictures, should set refreshing to false`() {

    }
}