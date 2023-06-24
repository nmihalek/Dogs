package com.nmihalek.dogs.feature.breeddetails.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PicturesRaw(
    @PrimaryKey
    var id: String = "",
    @SerializedName("message")
    val imageUrls: List<String>,
    val status: String
)
