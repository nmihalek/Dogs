package com.nmihalek.dogs.feature.breeddetails.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.nmihalek.dogs.feature.breeddetails.data.model.PicturesRaw
import kotlinx.coroutines.flow.Flow

@Dao
interface PicturesDao {
    @Insert
    suspend fun insertPictures(pictures: PicturesRaw)

    @Upsert
    suspend fun updatePictures(pictures: PicturesRaw)

    @Query("SELECT * FROM PicturesRaw WHERE id = :breed")
    fun getPictures(breed: String): Flow<PicturesRaw?>

    @Query("DELETE FROM PicturesRaw WHERE id = :breed")
    suspend fun deletePicturesFor(breed: String)

    @Query("DELETE FROM PicturesRaw")
    suspend fun deleteAllPictures()
}