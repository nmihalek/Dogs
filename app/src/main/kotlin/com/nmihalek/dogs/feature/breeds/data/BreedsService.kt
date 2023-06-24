package com.nmihalek.dogs.feature.breeds.data

import com.nmihalek.dogs.feature.breeds.data.model.BreedsRaw
import retrofit2.http.GET

interface BreedsService {

    @GET("breeds/list/all")
    suspend fun getAllBreeds() : BreedsRaw

}
