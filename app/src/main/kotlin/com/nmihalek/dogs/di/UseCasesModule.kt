package com.nmihalek.dogs.di

import com.nmihalek.dogs.feature.breeddetails.domain.PicturesRepository
import com.nmihalek.dogs.feature.breeddetails.domain.usecase.GetBreedsPicturesUseCase
import com.nmihalek.dogs.feature.breeddetails.domain.usecase.GetBreedsPicturesUseCaseImpl
import com.nmihalek.dogs.feature.breeddetails.domain.usecase.RefreshBreedsPicturesUseCase
import com.nmihalek.dogs.feature.breeds.domain.BreedsRepository
import com.nmihalek.dogs.feature.breeds.domain.usecase.RefreshBreedsUseCase
import com.nmihalek.dogs.feature.breeds.domain.usecase.GetBreedsUseCase
import com.nmihalek.dogs.feature.favourites.domain.FavouritesRepository
import com.nmihalek.dogs.feature.favourites.domain.usecase.GetFavouritePicturesUseCase
import com.nmihalek.dogs.feature.favourites.domain.usecase.UpdateFavouriteUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule private constructor() {

    @Binds
    abstract fun bindGetBreedsPicturesUseCase(
        breedPicturesUseCase: GetBreedsPicturesUseCaseImpl
    ): GetBreedsPicturesUseCase

    companion object {

        @Provides
        @Singleton
        fun provideGetBreedsUseCase(breedsRepository: BreedsRepository): GetBreedsUseCase =
            GetBreedsUseCase(breedsRepository::observeBreeds)

        @Provides
        @Singleton
        fun provideRefreshBreedsUseCase(breedsRepository: BreedsRepository): RefreshBreedsUseCase =
            RefreshBreedsUseCase(breedsRepository::fetchBreeds)

        @Provides
        @Singleton
        fun provideRefreshBreedPicturesUseCase(picturesRepository: PicturesRepository): RefreshBreedsPicturesUseCase =
            RefreshBreedsPicturesUseCase(picturesRepository::fetchBreedPictures)

        @Provides
        @Singleton
        fun provideUpdateFavouriteUseCase(favouritesRepository: FavouritesRepository): UpdateFavouriteUseCase =
            UpdateFavouriteUseCase(favouritesRepository::updateFavourite)

        @Provides
        @Singleton
        fun provideGetFavouritePicturesUseCase(
            favouritesRepository: FavouritesRepository
        ): GetFavouritePicturesUseCase =
            GetFavouritePicturesUseCase(favouritesRepository::observeFavourites)
    }
}
