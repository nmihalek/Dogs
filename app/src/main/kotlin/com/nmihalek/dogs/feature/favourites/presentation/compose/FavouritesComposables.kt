package com.nmihalek.dogs.feature.favourites.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Search(modifier: Modifier = Modifier, searchText: MutableStateFlow<String>) {

}