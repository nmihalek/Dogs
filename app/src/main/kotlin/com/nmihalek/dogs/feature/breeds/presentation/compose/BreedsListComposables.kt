package com.nmihalek.dogs.feature.breeds.presentation.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmihalek.dogs.feature.breeds.presentation.model.BreedListItemUi
import com.nmihalek.dogs.feature.common.presentation.capitalise
import com.nmihalek.dogs.feature.common.presentation.compose.Loader
import com.nmihalek.dogs.feature.common.presentation.compose.onClick

@Suppress("FunctionNaming")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BreedList(
    modifier: Modifier = Modifier,
    breeds: State<List<BreedListItemUi>>,
    refreshing: Boolean,
    onPullRefresh: () -> Unit,
    onItemClicked: (String, String) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(onRefresh = onPullRefresh, refreshing = refreshing)
    Box(modifier = modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
        ) {
            val breedsValue = breeds.value
            items(breedsValue) { breed ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .onClick {
                        onItemClicked(breed.name, breed.subBreed)
                    }) {
                    Text(
                        modifier = Modifier
                            .padding(12.dp),
                        text = if (breed.subBreed.isNotBlank())
                            "${breed.name.capitalise()}/${breed.subBreed.capitalise()}"
                        else breed.name.capitalise()
                    )
                }

                if (breed != breedsValue.lastOrNull()) {
                    Divider()
                }
            }
        }
        Loader(
            modifier = Modifier.fillMaxWidth(),
            pullRefreshState = pullRefreshState,
            refreshing = refreshing
        )
    }
}

@Preview
@Composable
private fun Preview() {
    val list = remember { mutableStateOf(listOf(BreedListItemUi(name = "Husky"), BreedListItemUi(name = "Doggo"))) }
    BreedList(
        breeds = list,
        refreshing = false,
        onPullRefresh = {}
    ) { _, _ -> }
}