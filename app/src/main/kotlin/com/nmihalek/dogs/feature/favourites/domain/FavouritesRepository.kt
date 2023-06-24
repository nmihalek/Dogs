package com.nmihalek.dogs.feature.favourites.domain

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun observeFavourites(): Flow<List<Picture>>
    suspend fun updateFavourite(picture: Picture): Result<Unit>
}