package com.nmihalek.dogs.feature.breeds.presentation

import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import com.nmihalek.dogs.feature.breeds.domain.usecase.GetBreedsUseCase
import com.nmihalek.dogs.feature.breeds.domain.usecase.RefreshBreedsUseCase
import com.nmihalek.dogs.feature.breeds.presentation.model.BreedListItemUi
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.invoke
import io.mockk.slot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KFunction

class BreedListViewModelTest {

    @RelaxedMockK
    lateinit var getBreedsUseCase: GetBreedsUseCase
    @RelaxedMockK
    lateinit var refreshBreedsUseCase: RefreshBreedsUseCase

    lateinit var breedsFlow: MutableSharedFlow<List<Breed>>

    lateinit var sut: BreedListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        breedsFlow = MutableSharedFlow(replay = 1)
    }

    @Test
    fun `given breeds list, when breed emitted, should convert and set refreshing to false`() {
        every { getBreedsUseCase(any()) } returns breedsFlow
        sut = BreedListViewModel(getBreedsUseCase, refreshBreedsUseCase)
        assertTrue(sut.refreshing)
        val output = listOf(Breed("akita"))
        val expected = listOf(BreedListItemUi("akita"))

        runBlocking {
            breedsFlow.tryEmit(output)

            val result = sut.breeds.first()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `given no breeds in storage, when error fetching, should pass error message`() {
        val errorMessage = "Oops, I did it again!"
        val expectedErrorMessage = "Error on initial fetch: Oops, I did it again!"
        val failedResult = Result.failure<Unit>(Exception(errorMessage))
        // create a callback to be passed to the usecase and get its reference
        var callback: ((Result<Unit>) -> Unit)? = null
        getBreedsUseCase = GetBreedsUseCase {
            callback = it
            breedsFlow
        }
        sut = BreedListViewModel(getBreedsUseCase, refreshBreedsUseCase)
        assertEquals("", sut.error)

        // activate the getter
        sut.breeds
        callback!!.invoke(failedResult)

        assertEquals(expectedErrorMessage, sut.error)
    }

    @Test
    fun `given no breeds in storage, when success fetching, should set error to blank`() {
        // create a callback to be passed to the usecase and get its reference
        var callback: ((Result<Unit>) -> Unit)? = null
        getBreedsUseCase = GetBreedsUseCase {
            callback = it
            breedsFlow
        }
        sut = BreedListViewModel(getBreedsUseCase, refreshBreedsUseCase)

        // activate the getter
        sut.breeds
        callback!!.invoke(Result.success(Unit))

        assertEquals("", sut.error)
    }
}