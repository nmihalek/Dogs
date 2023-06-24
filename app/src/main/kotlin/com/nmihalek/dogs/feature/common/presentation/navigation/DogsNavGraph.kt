package com.nmihalek.dogs.feature.common.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.nmihalek.dogs.R
import com.nmihalek.dogs.feature.breeddetails.presentation.BreedDetailsViewModel
import com.nmihalek.dogs.feature.breeddetails.presentation.compose.route.BreedDetailsRoute
import com.nmihalek.dogs.feature.breeds.presentation.BreedListViewModel
import com.nmihalek.dogs.feature.breeds.presentation.compose.route.HomeRoute
import com.nmihalek.dogs.feature.common.presentation.capitalise
import com.nmihalek.dogs.feature.favourites.presentation.compose.route.FavouritesRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DogsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    startDestination: String = NavBarItem.Home.route,
    setTitle: (String) -> Unit,
) {
    val actions = remember { DogsNavigationActions(navController) }
    val coroutineScope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = NavBarItem.Home.route,
            deepLinks = listOf()
        ) {
            val homeViewModel: BreedListViewModel = hiltViewModel()
            setTitle(stringResource(id = R.string.app_name))
            AnimatedVisibility(visible = true) {
                HomeRoute(
                    viewModel = homeViewModel,
                    navigateBreedDetails = actions.navigateBreedDetails
                )
                handleErrors(
                    scaffoldState = scaffoldState,
                    error = homeViewModel.error,
                    coroutineScope = coroutineScope
                )
            }
        }
        composable(NavBarItem.Favourites.route) {
            setTitle(stringResource(id = R.string.favourites))
            AnimatedVisibility(visible = true) {
                FavouritesRoute(favouritesViewModel = hiltViewModel())
            }
        }
        composable(
            route = DogsDestinations.BREED_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(DogsDestinations.ARGUMENT_BREED_NAME) { type = NavType.StringType },
                navArgument(DogsDestinations.ARGUMENT_SUB_BREED_PARAM) { defaultValue = "" },
            )
        ) {
            val breedDetailsViewModel: BreedDetailsViewModel = hiltViewModel()
            val breed = breedDetailsViewModel.breed.capitalise()
            val subBreed = breedDetailsViewModel.subBreed.capitalise()
            val title = if(subBreed.isEmpty()) breed else "$subBreed $breed"
            setTitle("$title pictures")
            AnimatedVisibility(visible = true) {
                BreedDetailsRoute(breedDetailsViewModel = breedDetailsViewModel)
            }
        }
    }
}

@Composable
fun BottomNavBar(modifier: Modifier, navController: NavController) {
    val items = listOf(NavBarItem.Home, NavBarItem.Favourites)
    BottomNavigation(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(text = item.title, fontSize = 12.sp)
                },
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.onPrimary.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun handleErrors(scaffoldState: ScaffoldState, error: String, coroutineScope: CoroutineScope) {
    if(error.isNotBlank()) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message = error, duration = SnackbarDuration.Short)
        }
    }
}