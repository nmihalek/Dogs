package com.nmihalek.dogs.feature.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nmihalek.dogs.feature.breeddetails.data.dao.PicturesDao
import com.nmihalek.dogs.feature.breeddetails.data.model.PicturesRaw
import com.nmihalek.dogs.feature.breeds.data.dao.BreedsDao
import com.nmihalek.dogs.feature.breeds.data.model.BreedsRaw
import com.nmihalek.dogs.feature.favourites.data.dao.FavouritesDao
import com.nmihalek.dogs.feature.favourites.data.model.FavouritePictureEntity
import javax.inject.Singleton

@Database(entities = [BreedsRaw::class, PicturesRaw::class, FavouritePictureEntity::class], version = 6)
@TypeConverters(value = [ StringMapConverter::class, StringListConverter::class ])
abstract class AppDatabase: RoomDatabase() {
    abstract fun breedsDao(): BreedsDao
    abstract fun picturesDao(): PicturesDao
    abstract fun favouritesDao(): FavouritesDao
}