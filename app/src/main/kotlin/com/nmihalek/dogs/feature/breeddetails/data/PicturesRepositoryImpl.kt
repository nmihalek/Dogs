package com.nmihalek.dogs.feature.breeddetails.data

import com.nmihalek.dogs.feature.breeddetails.data.dao.PicturesDao
import com.nmihalek.dogs.feature.breeddetails.data.model.PicturesRaw
import com.nmihalek.dogs.feature.breeddetails.domain.PicturesRepository
import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PicturesRepositoryImpl @Inject constructor(
    private val picturesService: PicturesService,
    private val picturesDao: PicturesDao
) : PicturesRepository {

    override fun observeBreedPictures(breed: Breed): Flow<List<Picture>> =
        picturesDao.getPictures(breed.fullBreed)
            .onEach {
                if (it == null) {
                    fetchBreedPictures(breed)
                }
            }
            .filterNotNull()
            .map { picture ->
                picture.imageUrls.map { Picture(breed = breed.name, subBreed = breed.subBreed, imageUrl = it) }
            }

    override suspend fun fetchBreedPictures(breed: Breed): Result<List<Picture>> = runCatching {
        val newPictures = picturesService.getPicturesForBreed(breed.fullBreed).also {
            it.id = breed.fullBreed
        }
        if (newPictures.status != "success") {
            throw HttpException(Response.error<PicturesRaw>(400, newPictures.status.toResponseBody()))
        }
        picturesDao.deletePicturesFor(breed.fullBreed)
        picturesDao.insertPictures(newPictures)
        newPictures.imageUrls.map { Picture(breed = breed.name, subBreed = breed.subBreed, imageUrl = it) }
    }
}