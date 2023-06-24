package com.nmihalek.dogs.feature.common.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

object DogsDestinations {
    const val ARGUMENT_BREED_NAME = "name"
    const val ARGUMENT_SUB_BREED_NAME = "subBreed"
    internal const val ARGUMENT_SUB_BREED_PARAM = "{subBreed}"
    internal const val ARGUMENT_BREED_PARAM = "{name}"
    const val BREED_DETAILS_ROUTE =
        "breed/$ARGUMENT_BREED_PARAM?$ARGUMENT_SUB_BREED_NAME=$ARGUMENT_SUB_BREED_PARAM"
}

class DogsNavigationActions(navController: NavController) {
    val navigateBreedDetails: (String, String) -> Unit = { breed, subBreed ->
        val route = DogsDestinations.BREED_DETAILS_ROUTE
            .replace(DogsDestinations.ARGUMENT_BREED_PARAM, breed)
            .replace(DogsDestinations.ARGUMENT_SUB_BREED_PARAM, subBreed)
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

sealed class NavBarItem(val title: String, val icon: ImageVector, val route: String) {
    object Home: NavBarItem("Home", Icons.Filled.Home, "home")
    object Favourites: NavBarItem("Favourites", Icons.Filled.Star, "favourites")

    companion object {
        val allDestinations = listOf(Home.route, Favourites.route)
    }
}