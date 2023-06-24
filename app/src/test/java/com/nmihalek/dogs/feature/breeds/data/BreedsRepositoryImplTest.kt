package com.nmihalek.dogs.feature.breeds.data

import com.nmihalek.dogs.feature.breeds.data.dao.BreedsDao
import com.nmihalek.dogs.feature.breeds.data.model.BreedsRaw
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.justRun
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BreedsRepositoryImplTest {
    @RelaxedMockK
    lateinit var breedsDao: BreedsDao
    @RelaxedMockK
    lateinit var breedsService: BreedsService
    @InjectMockKs
    lateinit var sut: BreedsRepositoryImpl

    lateinit var breedsFlow: MutableSharedFlow<BreedsRaw?>

    val breedsSuccess = BreedsRaw(id = 1, emptyMap(), "success")
    val breedsFail = BreedsRaw(id = 2, emptyMap(), "fail")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        breedsFlow = MutableSharedFlow(1)
        every { breedsDao.queryAllBreeds() } returns breedsFlow
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given local storage empty and fetch success, when observe called, should fetch breeds and return success result`() {
        val fetchBreeds: (Result<Unit>) -> Unit = {
            breedsFlow.tryEmit(breedsSuccess)
            assertTrue(it.isSuccess)
        }
        coEvery { breedsService.getAllBreeds() } returns breedsSuccess

        runBlocking {
            breedsFlow.emit(null)
            val result = sut.observeBreeds(fetchBreeds).first()
            assertTrue(result.isEmpty())
        }
    }

    @Test
    fun `given local storage empty and fetch error, when observe called, should return error`() {
        val fetchBreeds: (Result<Unit>) -> Unit = {
            breedsFlow.tryEmit(breedsFail)
            assertTrue(it.isFailure)
        }
        coEvery { breedsService.getAllBreeds() } throws Exception("Oops, I did it again!")

        runBlocking {
            breedsFlow.emit(null)
            val result = sut.observeBreeds(fetchBreeds).first()
            assertTrue(result.isEmpty())
        }
    }

    @Test
    fun `given local storage contains breeds, when observe called, should return list of breeds`() {
        val daoOutput = BreedsRaw(
            id = 1,
            messageRaw = mapOf("akita" to emptyList(), "spaniel" to listOf("cocker", "welsh")),
            status = "success"
        )
        val expectedBreeds = listOf(Breed("akita"), Breed("spaniel", "cocker"), Breed("spaniel", "welsh"))
        breedsFlow.tryEmit(daoOutput)

        val result = runBlocking { sut.observeBreeds {  }.first() }

        assertEquals(expectedBreeds, result)
    }

    @Test
    fun `given fetch success, when fetch called, should insert breeds`() {
        coEvery { breedsService.getAllBreeds() } returns breedsSuccess

        val result = runBlocking { sut.fetchBreeds() }

        assertTrue(result.isSuccess)
        coVerify { breedsDao.insertBreeds(breedsSuccess) }
        coVerify { breedsService.getAllBreeds() }
        confirmVerified(breedsDao)
        confirmVerified(breedsService)
    }

    @Test
    fun `given fetch success but status not success, when fetch called, should do nothing`() {
        coEvery { breedsService.getAllBreeds() } returns breedsFail

        val result = runBlocking { sut.fetchBreeds() }

        assertTrue(result.isSuccess)
        coVerify(exactly = 0) { breedsDao.insertBreeds(any()) }
        confirmVerified(breedsDao)
    }

    @Test
    fun `given fetch error, when fetch called, should return failure result`() {
        coEvery { breedsService.getAllBreeds() } throws Exception("Oops, I did it again!")

        val result = runBlocking { sut.fetchBreeds() }

        assertTrue(result.isFailure)
    }

    @Test
    fun `given clear delete success, when callign clear, should return success`() {
        coJustRun { breedsDao.deleteAllBreeds() }

        val result = runBlocking { sut.clear() }

        assertTrue(result.isSuccess)
    }

    @Test
    fun `given clear delete throws, when calling clear, should return failure`() {
        coEvery { breedsDao.deleteAllBreeds() } throws Exception("Oops, I did it again!")

        val result = runBlocking { sut.clear() }

        assertTrue(result.isFailure)
    }
}
