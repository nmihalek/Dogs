package com.nmihalek.dogs.feature.breeddetails.domain.usecase

import com.nmihalek.dogs.feature.breeddetails.domain.PicturesRepository
import com.nmihalek.dogs.feature.breeddetails.domain.model.Picture
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import com.nmihalek.dogs.feature.favourites.domain.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface GetBreedsPicturesUseCase {
    operator fun invoke(breed: Breed): Flow<List<Picture>>
}

class GetBreedsPicturesUseCaseImpl @Inject constructor(
    private val picturesRepository: PicturesRepository,
    private val favouritesRepository: FavouritesRepository
) : GetBreedsPicturesUseCase {

    override operator fun invoke(breed: Breed): Flow<List<Picture>> =
        favouritesRepository.observeFavourites()
            .combine(picturesRepository.observeBreedPictures(breed = breed)) { favouritePictures, breedPictures ->
                breedPictures.map { breedPicture ->
                    favouritePictures.find { breedPicture.imageUrl == it.imageUrl } ?: breedPicture
                }
            }
}