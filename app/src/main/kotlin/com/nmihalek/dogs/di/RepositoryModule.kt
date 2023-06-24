package com.nmihalek.dogs.di

import com.nmihalek.dogs.feature.breeddetails.data.PicturesRepositoryImpl
import com.nmihalek.dogs.feature.breeddetails.domain.PicturesRepository
import com.nmihalek.dogs.feature.breeds.data.BreedsRepositoryImpl
import com.nmihalek.dogs.feature.breeds.domain.BreedsRepository
import com.nmihalek.dogs.feature.favourites.data.FavouritesRepositoryImpl
import com.nmihalek.dogs.feature.favourites.domain.FavouritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindBreedsRepository(repository: BreedsRepositoryImpl): BreedsRepository

    @Binds
    fun bindPicturesRepository(repository: PicturesRepositoryImpl): PicturesRepository

    @Binds
    fun bindFavouritesRepository(repository: FavouritesRepositoryImpl): FavouritesRepository
}
