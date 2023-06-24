package com.nmihalek.dogs.feature.breeds.domain.usecase

import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import kotlinx.coroutines.flow.Flow

fun interface GetBreedsUseCase : ((Result<Unit>) -> Unit) -> Flow<List<Breed>>

fun interface RefreshBreedsUseCase : suspend () -> Result<Unit>
