package com.nmihalek.dogs.feature.breeds.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nmihalek.dogs.feature.breeds.data.model.BreedsRaw
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsDao {
    @Insert
    suspend fun insertBreeds(breeds: BreedsRaw)

    @Update
    suspend fun updateBreeds(breeds: BreedsRaw)

    @Query("SELECT * FROM Breeds")
    fun queryAllBreeds(): Flow<BreedsRaw?>

    @Query("DELETE from Breeds")
    suspend fun deleteAllBreeds()
}