package com.nmihalek.dogs.feature.favourites.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture

@Entity
data class FavouritePictureEntity(
    @PrimaryKey
    val imageUrl: String,
    val breed: String,
    val subBreed: String
)

fun FavouritePictureEntity.toPicture(isFavourite: Boolean = true) =
    Picture(breed = breed, subBreed = subBreed, imageUrl = imageUrl, isFavourite = isFavourite)
fun Picture.toFavouritePictureEntity() = FavouritePictureEntity(imageUrl = imageUrl, breed = breed, subBreed = subBreed)
