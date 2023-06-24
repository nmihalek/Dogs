package com.nmihalek.dogs.feature.favourites.data

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.favourites.data.dao.FavouritesDao
import com.nmihalek.dogs.feature.favourites.data.model.FavouritePictureEntity
import com.nmihalek.dogs.feature.favourites.data.model.toFavouritePictureEntity
import com.nmihalek.dogs.feature.favourites.data.model.toPicture
import com.nmihalek.dogs.feature.favourites.domain.FavouritesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavouritesRepositoryImplTest {
    @RelaxedMockK
    lateinit var favouritesDao: FavouritesDao
    lateinit var sut: FavouritesRepository

    lateinit var allFavourites: MutableSharedFlow<List<FavouritePictureEntity>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        allFavourites = MutableSharedFlow(1)
        every { favouritesDao.getAllFavourites() } returns allFavourites
        sut = FavouritesRepositoryImpl(favouritesDao)
    }

    @Test
    fun `given 2 favourites, when observed, should return and convert to picture`() {
        val output = listOf(FavouritePictureEntity("", "akita", ""), FavouritePictureEntity("", "spaniel", "cocker"))
        val expected = listOf(
            Picture("akita", "", "", isFavourite = true),
            Picture("spaniel", "cocker", "", isFavourite = true)
        )
        allFavourites.tryEmit(output)

        val result = runBlocking { sut.observeFavourites().first() }

        assertEquals(expected, result)
    }

    @Test
    fun `given favourite picture passed, when update called, should insert`() = runBlocking {
        coEvery { favouritesDao.insert(any()) } returns Unit
        val input = Picture("akita", "", "", isFavourite = true)

        sut.updateFavourite(input)

        coVerify { favouritesDao.insert(input.toFavouritePictureEntity()) }
        confirmVerified(favouritesDao)
    }

    @Test
    fun `given non favourite picture passed, when update called, should remove`() = runBlocking {
        coEvery { favouritesDao.delete(any()) } returns Unit
        val input = Picture("akita", "", "", isFavourite = false)

        sut.updateFavourite(input)

        coVerify { favouritesDao.delete(input.toFavouritePictureEntity()) }
        confirmVerified(favouritesDao)
    }
}