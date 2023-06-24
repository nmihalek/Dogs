package com.nmihalek.dogs.feature.breeddetails.domain.usecase

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeds.domain.model.Breed

fun interface RefreshBreedsPicturesUseCase: suspend (Breed) -> Result<List<Picture>>