package com.nmihalek.dogs.feature.favourites.domain.usecase

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import kotlinx.coroutines.flow.Flow

fun interface UpdateFavouriteUseCase: suspend (Picture) -> Result<Unit>

fun interface GetFavouritePicturesUseCase: () -> Flow<List<Picture>>
