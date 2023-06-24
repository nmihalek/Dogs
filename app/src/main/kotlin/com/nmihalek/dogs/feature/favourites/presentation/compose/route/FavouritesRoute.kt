package com.nmihalek.dogs.feature.favourites.presentation.compose.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nmihalek.dogs.feature.breeddetails.presentation.compose.PictureList
import com.nmihalek.dogs.feature.favourites.presentation.FavouritesViewModel
import com.nmihalek.dogs.feature.favourites.presentation.compose.Search

@Composable
fun FavouritesRoute(favouritesViewModel: FavouritesViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        PictureList(
            modifier = Modifier.fillMaxSize(),
            pictureList = favouritesViewModel.favouritePictures.collectAsStateWithLifecycle(initialValue = emptyList()).value,
            refreshing = false,
            showTitle = true,
            onRefresh = { },
            onFavouriteClicked = favouritesViewModel::onFavouriteClicked
        )
    }
}