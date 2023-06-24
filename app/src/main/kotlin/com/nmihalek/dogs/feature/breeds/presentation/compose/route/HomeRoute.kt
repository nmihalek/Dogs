package com.nmihalek.dogs.feature.breeds.presentation.compose.route

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nmihalek.dogs.feature.breeds.presentation.BreedListViewModel
import com.nmihalek.dogs.feature.breeds.presentation.compose.BreedList

@Composable
fun HomeRoute(viewModel: BreedListViewModel, navigateBreedDetails: (String, String) -> Unit) {
    BreedList(
        breeds = viewModel.breeds.collectAsStateWithLifecycle(initialValue = emptyList()),
        refreshing = viewModel.refreshing,
        onPullRefresh = viewModel::refreshBreeds,
        onItemClicked = navigateBreedDetails
    )
}