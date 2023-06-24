package com.nmihalek.dogs.feature.breeds.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Breeds")
data class BreedsRaw(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("message")
    val messageRaw: Map<String, List<String>>,
    val status: String
)