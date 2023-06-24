package com.nmihalek.dogs.feature.favourites.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.nmihalek.dogs.feature.breeddetails.presentation.model.PictureUiItem
import com.nmihalek.dogs.feature.breeddetails.presentation.model.toBreedPictureUiItem
import com.nmihalek.dogs.feature.breeddetails.presentation.model.toPicture
import com.nmihalek.dogs.feature.common.util.mapList
import com.nmihalek.dogs.feature.favourites.domain.usecase.GetFavouritePicturesUseCase
import com.nmihalek.dogs.feature.favourites.domain.usecase.UpdateFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.delayFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FavouritesVM"

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouritePicturesUseCase: GetFavouritePicturesUseCase,
    private val updateFavouriteUseCase: UpdateFavouriteUseCase
) : ViewModel() {

    val favouritePictures: Flow<List<PictureUiItem>> get() = getFavouritePicturesUseCase()
        .mapList { it.toBreedPictureUiItem() }

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
