package com.nmihalek.dogs.feature.favourites.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nmihalek.dogs.feature.favourites.data.model.FavouritePictureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM FavouritePictureEntity")
    fun getAllFavourites(): Flow<List<FavouritePictureEntity>>

    @Insert
    suspend fun insert(favouritePicture: FavouritePictureEntity)

    @Delete
    suspend fun delete(favouritePicture: FavouritePictureEntity)
}
