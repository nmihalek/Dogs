package com.nmihalek.dogs.feature.breeddetails.presentation.model

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture

data class PictureUiItem(
    val breed: String,
    val subBreed: String,
    val imageUrl: String,
    val isFavourite: Boolean
)

val PictureUiItem.fullBreed get() = if (subBreed.isNotEmpty()) "$breed/$subBreed" else breed
fun PictureUiItem.toPicture() =
    Picture(breed = breed, subBreed = subBreed, imageUrl = imageUrl, isFavourite = isFavourite)
fun Picture.toBreedPictureUiItem() =
    PictureUiItem(breed = breed, subBreed = subBreed, imageUrl = imageUrl.trim(), isFavourite = isFavourite)