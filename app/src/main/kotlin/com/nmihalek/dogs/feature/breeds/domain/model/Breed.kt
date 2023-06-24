package com.nmihalek.dogs.feature.breeds.domain.model

data class Breed(
    val name: String,
    val subBreed: String = ""
) {
    val fullBreed: String
        get() = if (subBreed.isBlank()) name else "$name/$subBreed"
}
