package com.nmihalek.dogs.feature.breeddetails.data

import com.nmihalek.dogs.MainCoroutineRule
import com.nmihalek.dogs.feature.breeddetails.data.dao.PicturesDao
import com.nmihalek.dogs.feature.breeddetails.data.model.PicturesRaw
import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class PicturesRepositoryImplTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val rule = MainCoroutineRule()

    @RelaxedMockK
    lateinit var picturesService: PicturesService
    @RelaxedMockK
    lateinit var picturesDao: PicturesDao
    @InjectMockKs
    lateinit var sut: PicturesRepositoryImpl

    lateinit var flow: MutableSharedFlow<PicturesRaw?>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        flow = MutableSharedFlow(replay = 1)
        every { picturesDao.getPictures(any()) } returns flow
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given pictures in storage, when observe breed pictures called, should return mapped pictures`() {
        val fullBreed = "spaniel/cocker"
        val picturesRaw = PicturesRaw(id = "", imageUrls = listOf("1"), status = "success")
        coEvery { picturesService.getPicturesForBreed(fullBreed) } returns picturesRaw
        val input = Breed(name = "spaniel", subBreed = "cocker")
        val expected = listOf(Picture(breed = "spaniel", subBreed = "cocker", imageUrl = "1"))

        flow.tryEmit(picturesRaw)
        val result = runBlocking { sut.observeBreedPictures(input).first() }

        assertEquals(expected, result)
    }

    @Test
    fun `given fetch breed pictures success, when called, should return list`() {
        val fullBreed = "spaniel/cocker"
        val picturesRaw = PicturesRaw(id = "", imageUrls = listOf("1"), status = "success")
        val input = Breed(name = "spaniel", subBreed = "cocker")
        val expected = listOf(Picture(breed = "spaniel", subBreed = "cocker", imageUrl = "1"))
        coEvery { picturesService.getPicturesForBreed(fullBreed) } returns picturesRaw

        val result = runBlocking { sut.fetchBreedPictures(input) }.getOrThrow()

        assertEquals(expected, result)
    }

    @Test
    fun `given fetch breed pictures ok but not successful, should throw http exception`() {
        val input = Breed(name = "spaniel", subBreed = "cocker")
        val picturesRaw = PicturesRaw(id = "", imageUrls = listOf(""), status = "failure")
        coEvery { picturesService.getPicturesForBreed(any()) } returns picturesRaw

        val result = runBlocking { sut.fetchBreedPictures(input) }

        assertTrue(result.isFailure)
        assertEquals(HttpException::class, result.exceptionOrNull()!!::class)
    }

    @Test
    fun `given fetch breed pictures unexpected exception, should return result failure`() {
        val input = Breed(name = "spaniel", subBreed = "cocker")
        coEvery { picturesService.getPicturesForBreed(any()) } throws Exception("Oops, I did it again!")

        val result = runBlocking { sut.fetchBreedPictures(input) }

        assertTrue(result.isFailure)
    }
}