package com.nmihalek.dogs.feature.breeddetails.domain.model

data class Picture(
    val breed: String,
    val subBreed: String = "",
    val imageUrl: String,
    var isFavourite: Boolean = false
)
