package com.nmihalek.dogs.feature.common.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nmihalek.dogs.feature.common.presentation.TitleViewModel
import com.nmihalek.dogs.feature.common.presentation.navigation.BottomNavBar
import com.nmihalek.dogs.feature.common.presentation.navigation.DogsNavGraph
import com.nmihalek.dogs.feature.common.presentation.navigation.NavBarItem

@Composable
fun DogsApplication() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val titleViewModel: TitleViewModel = hiltViewModel()

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    bottomBarState.value = NavBarItem.allDestinations.contains(navBackStackEntry?.destination?.route)
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AnimatedVisibility(
                visible = topBarState.value,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TopAppBar(contentPadding = PaddingValues(20.dp)) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onPrimary,
                        text = titleViewModel.title
                    )
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarState.value,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                BottomNavBar(modifier = Modifier.padding(bottom = 20.dp), navController = navController)
            }
        }
    ) {
        DogsNavGraph(
            modifier = Modifier.padding(it),
            navController = navController,
            scaffoldState = scaffoldState,
            setTitle = titleViewModel::setAppTitle
        )
    }
}