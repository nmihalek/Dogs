package com.nmihalek.dogs.feature.breeddetails.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nmihalek.dogs.feature.breeddetails.domain.usecase.GetBreedsPicturesUseCase
import com.nmihalek.dogs.feature.breeddetails.domain.usecase.RefreshBreedsPicturesUseCase
import com.nmihalek.dogs.feature.breeddetails.presentation.model.PictureUiItem
import com.nmihalek.dogs.feature.breeddetails.presentation.model.toBreedPictureUiItem
import com.nmihalek.dogs.feature.breeddetails.presentation.model.toPicture
import com.nmihalek.dogs.feature.breeds.domain.model.Breed
import com.nmihalek.dogs.feature.common.presentation.navigation.DogsDestinations
import com.nmihalek.dogs.feature.common.util.mapList
import com.nmihalek.dogs.feature.favourites.domain.usecase.UpdateFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BreedDetailsVM"

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBreedsPicturesUseCase: GetBreedsPicturesUseCase,
    private val refreshBreedsUseCase: RefreshBreedsPicturesUseCase,
    private val updateFavouriteUseCase: UpdateFavouriteUseCase
): ViewModel() {

    val breed: String = savedStateHandle.get<String>(DogsDestinations.ARGUMENT_BREED_NAME).orEmpty()
    val subBreed: String = savedStateHandle.get<String>(DogsDestinations.ARGUMENT_SUB_BREED_NAME).orEmpty()
    private val fullBreed: Breed = Breed(name = breed, subBreed = subBreed)

    init {
        Log.d(TAG, "Starting up with argument: $breed")
    }

    val pictures: Flow<List<PictureUiItem>>
        get() = getBreedsPicturesUseCase(fullBreed)
            .onEach { refreshing = false }
            .mapList { it.toBreedPictureUiItem() }

    var refreshing: Boolean by mutableStateOf(true)
        private set

    fun refreshBreedPictures() {
        viewModelScope.launch {
            refreshing = true
            refreshBreedsUseCase(fullBreed)
                .fold(
                    onSuccess = {
                        refreshing = false
                        Log.d(TAG, "refreshBreedPictures: success!")
                    },
                    onFailure = {
                        refreshing = false
                        Log.e(TAG, "refreshBreedPictures: failure!", it)
                    }
                )
        }
    }

    fun onFavouriteClicked(item: PictureUiItem) {
        viewModelScope.launch {
            updateFavouriteUseCase(item.toPicture())
                .onSuccess {
                    Log.d(TAG, "setting favourite: success! Picture: $item")
                }
                .onFailure {
                    Log.e(TAG, "setting favourite: fail! Picture: $item", it)
                }
        }
    }
}