package com.nmihalek.dogs.feature.favourites.presentation

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeddetails.presentation.model.toBreedPictureUiItem
import com.nmihalek.dogs.feature.favourites.domain.usecase.GetFavouritePicturesUseCase
import com.nmihalek.dogs.feature.favourites.domain.usecase.UpdateFavouriteUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class FavouritesViewModelTest {

    @RelaxedMockK
    lateinit var getFavouritePicturesUseCase: GetFavouritePicturesUseCase
    @RelaxedMockK
    lateinit var updateFavouriteUseCase: UpdateFavouriteUseCase
    lateinit var sut: FavouritesViewModel

    val picturesFlow: MutableSharedFlow<List<Picture>> = MutableSharedFlow(replay = 1)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { getFavouritePicturesUseCase.invoke() } returns picturesFlow
        coEvery { updateFavouriteUseCase.invoke(any()) } returns Result.success(Unit)
    }

    @Test
    fun `given pictures, when pictures received, should convert and display`() {
        sut = FavouritesViewModel(getFavouritePicturesUseCase, updateFavouriteUseCase)
        val picture = Picture("akita", "", "http://image.jpg", isFavourite = true)
        val picture2 = Picture("akita", "", "http://image2.jpg", isFavourite = false)
        val emission = listOf(picture, picture2)
        val expected = emission.map { it.toBreedPictureUiItem() }

        picturesFlow.tryEmit(emission)
        runBlocking {
            val result = sut.favouritePictures.first()
            assertEquals(expected, result)
        }
    }
}