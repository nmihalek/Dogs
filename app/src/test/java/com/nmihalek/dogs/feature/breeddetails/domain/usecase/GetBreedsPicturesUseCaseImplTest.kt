package com.nmihalek.dogs.feature.breeddetails.domain.usecase

import com.nmihalek.dogs.feature.breeddetails.domain.PicturesRepository
import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import com.nmihalek.dogs.feature.favourites.domain.FavouritesRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetBreedsPicturesUseCaseImplTest {

    @RelaxedMockK
    lateinit var picturesRepository: PicturesRepository
    @RelaxedMockK
    lateinit var favouritesRepository: FavouritesRepository
    @InjectMockKs
    lateinit var sut: GetBreedsPicturesUseCaseImpl

    lateinit var picturesFlow: MutableSharedFlow<List<Picture>>
    lateinit var favouritesFlow: MutableSharedFlow<List<Picture>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        picturesFlow = MutableSharedFlow(replay = 1)
        favouritesFlow = MutableSharedFlow(replay = 1)
        every { picturesRepository.observeBreedPictures(any()) } returns picturesFlow
        every { favouritesRepository.observeFavourites() } returns favouritesFlow
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `given 3 pictures and one favourite, when invoking usecase, should mark favourite picture as favourite`() {
        val cSpanPic1 = Picture("spaniel", "cocker", imageUrl = "5", isFavourite = false)
        val cSpanPic2 = Picture("spaniel", "cocker", imageUrl = "1", isFavourite = false)
        val cSpanPic3 = Picture("spaniel", "cocker", imageUrl = "2", isFavourite = false)
        val cSpanFavPic = Picture(breed = "spaniel", subBreed = "cocker", imageUrl = "1", isFavourite = true)
        val pictures: List<Picture> = listOf(
            cSpanPic1,
            cSpanPic2,
            cSpanPic3
        )
        val favouritePictures: List<Picture> = listOf(
            cSpanFavPic
        )
        val expected = listOf(cSpanPic1, cSpanFavPic, cSpanPic3)
        picturesFlow.tryEmit(pictures)
        favouritesFlow.tryEmit(favouritePictures)

        val result = runBlocking { sut.invoke(Breed(name = "spaniel", subBreed = "cocker")).first() }

        assertEquals(expected, result)
    }
}