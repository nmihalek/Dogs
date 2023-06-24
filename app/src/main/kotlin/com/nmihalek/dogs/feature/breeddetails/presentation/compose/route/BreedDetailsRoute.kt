package com.nmihalek.dogs.feature.breeddetails.presentation.compose.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nmihalek.dogs.feature.breeddetails.presentation.BreedDetailsViewModel
import com.nmihalek.dogs.feature.breeddetails.presentation.compose.PictureList

@Composable
fun BreedDetailsRoute(breedDetailsViewModel: BreedDetailsViewModel = hiltViewModel()) {
    PictureList(
        pictureList = breedDetailsViewModel.pictures.collectAsStateWithLifecycle(initialValue = emptyList()).value,
        refreshing = breedDetailsViewModel.refreshing,
        onRefresh = breedDetailsViewModel::refreshBreedPictures,
        onFavouriteClicked = breedDetailsViewModel::onFavouriteClicked
    )
}