package com.nmihalek.dogs.feature.breeds.data

import com.nmihalek.dogs.feature.breeds.data.dao.BreedsDao
import com.nmihalek.dogs.feature.breeds.domain.BreedsRepository
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedsRepositoryImpl @Inject constructor(
    private val breedsDao: BreedsDao,
    private val breedsService: BreedsService
): BreedsRepository {

    override fun observeBreeds(onInitialFetch: (Result<Unit>) -> Unit): Flow<List<Breed>> =
        breedsDao.queryAllBreeds()
            .onEach {
                if (it == null) {
                    onInitialFetch(fetchBreeds())
                }
            }
            .filterNotNull()
            .map { breeds ->
                breeds.messageRaw.entries.map { entry ->
                    if (entry.value.isEmpty()) {
                        listOf(Breed(name = entry.key))
                    } else {
                        entry.value.map {
                            Breed(name = entry.key, subBreed = it)
                        }
                    }
                }.flatten()
            }

    override suspend fun fetchBreeds(): Result<Unit> =
        runCatching {
            val breedsRaw = breedsService.getAllBreeds()
            if (breedsRaw.status == "success") {
                breedsDao.insertBreeds(breedsRaw)
            }
        }

    override suspend fun clear(): Result<Unit> =
        runCatching {
            breedsDao.deleteAllBreeds()
        }
}