package com.nmihalek.dogs.feature.breeddetails.domain

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import kotlinx.coroutines.flow.Flow

interface PicturesRepository {
    fun observeBreedPictures(breed: Breed): Flow<List<Picture>>
    suspend fun fetchBreedPictures(breed: Breed): Result<List<Picture>>
}