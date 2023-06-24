package com.nmihalek.dogs.feature.breeds.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nmihalek.dogs.feature.breeds.domain.usecase.RefreshBreedsUseCase
import com.nmihalek.dogs.feature.breeds.domain.usecase.GetBreedsUseCase
import com.nmihalek.dogs.feature.breeds.presentation.model.BreedListItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BreedsVM"

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val getBreedsUseCase: GetBreedsUseCase,
    private val refreshBreedsUseCase: RefreshBreedsUseCase
): ViewModel() {

    val breeds: Flow<List<BreedListItemUi>> get() = getBreedsUseCase(this::onInitialFetch)
        .onEach { refreshing = false }
        .map { breedsList ->
            breedsList.map {
                BreedListItemUi(
                    name = it.name,
                    subBreed = it.subBreed
                )
            }
        }

    var refreshing: Boolean by mutableStateOf(true)
        private set

    var error: String by mutableStateOf("")
        private set

    fun refreshBreeds() {
        refreshing = true
        viewModelScope.launch {
            refreshBreedsUseCase()
                .onSuccess {
                    refreshing = false
                    error = ""
                    Log.d(TAG, "refreshBreeds: success!")
                }
                .onFailure {
                    refreshing = false
                    error = "Error refreshing: ${it.message}"
                    Log.e(TAG, "refreshBreeds: failure!", it)
                }
        }
    }

    private fun onInitialFetch(result: Result<Unit>) {
        result.onSuccess {
            error = ""
        }.onFailure {
            error = "Error on initial fetch: ${it.message}"
        }
    }
}
