package com.nmihalek.dogs.feature.favourites.data

import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.favourites.data.dao.FavouritesDao
import com.nmihalek.dogs.feature.favourites.data.model.toFavouritePictureEntity
import com.nmihalek.dogs.feature.favourites.data.model.toPicture
import com.nmihalek.dogs.feature.favourites.domain.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesRepositoryImpl @Inject constructor(
    private val favouritesDao: FavouritesDao
) : FavouritesRepository {

    override fun observeFavourites(): Flow<List<Picture>> =
        favouritesDao.getAllFavourites()
            .filterNotNull()
            .map { favourites ->
                favourites.map { it.toPicture(isFavourite = true) }
            }

    override suspend fun updateFavourite(picture: Picture): Result<Unit> = runCatching {
        val entity = picture.toFavouritePictureEntity()
        if(picture.isFavourite) {
            favouritesDao.insert(entity)
        } else {
            favouritesDao.delete(entity)
        }
    }
}
