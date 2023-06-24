package com.nmihalek.dogs.feature.breeds.domain

import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import kotlinx.coroutines.flow.Flow

interface BreedsRepository {
    fun observeBreeds(onInitialFetch: (Result<Unit>) -> Unit): Flow<List<Breed>>
    suspend fun fetchBreeds(): Result<Unit>
    suspend fun clear(): Result<Unit>
}
