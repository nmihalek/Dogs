package com.nmihalek.dogs.feature.breeddetails.data

import com.nmihalek.dogs.feature.breeddetails.data.model.PicturesRaw
import retrofit2.http.GET
import retrofit2.http.Path

private const val BREED_PATH = "fullBreed"


interface PicturesService {

    /**
     * @param fullBreed path in the format parentBreed/breed, or just breed if there is no parent
     */
    @GET("breed/{$BREED_PATH}/images")
    suspend fun getPicturesForBreed(@Path(value = BREED_PATH, encoded = true) fullBreed: String): PicturesRaw
}