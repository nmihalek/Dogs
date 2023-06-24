package com.nmihalek.dogs.di

import android.app.Application
import androidx.room.Room
import com.nmihalek.dogs.feature.breeddetails.data.dao.PicturesDao
import com.nmihalek.dogs.feature.breeds.data.dao.BreedsDao
import com.nmihalek.dogs.feature.common.data.AppDatabase
import com.nmihalek.dogs.feature.favourites.data.dao.FavouritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule private constructor() {

    companion object {
        @Singleton
        @Provides
        fun provideRoom(application: Application): AppDatabase =
            Room
                .databaseBuilder(
                    context = application.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "dogs-db"
                )
                .fallbackToDestructiveMigration()
                .build()

        @Provides
        fun provideBreedsDao(appDatabase: AppDatabase): BreedsDao = appDatabase.breedsDao()

        @Provides
        fun providePicturesDao(appDatabase: AppDatabase): PicturesDao = appDatabase.picturesDao()

        @Provides
        fun provideFavouritesDao(appDatabase: AppDatabase): FavouritesDao = appDatabase.favouritesDao()
    }
}