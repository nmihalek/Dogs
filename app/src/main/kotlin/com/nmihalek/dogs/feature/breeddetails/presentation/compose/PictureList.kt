package com.nmihalek.dogs.feature.breeddetails.presentation.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nmihalek.dogs.feature.breeddetails.presentation.model.PictureUiItem
import com.nmihalek.dogs.feature.breeddetails.presentation.model.fullBreed
import com.nmihalek.dogs.feature.common.data.IMAGE_FETCH_TIMEOUT
import com.nmihalek.dogs.feature.common.presentation.compose.Loader
import com.nmihalek.dogs.feature.common.presentation.compose.onClick
import com.nmihalek.dogs.theme.DarkColors
import com.nmihalek.dogs.theme.LightColors
import com.skydoves.landscapist.glide.GlideImage

@Suppress("LongParameterList")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PictureList(
    modifier: Modifier = Modifier,
    pictureList: List<PictureUiItem>,
    refreshing: Boolean,
    showTitle: Boolean = false,
    onRefresh: () -> Unit,
    onFavouriteClicked: (PictureUiItem) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )
    val selectedTitle = remember { mutableStateOf("") }
    Box(modifier = modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState, !refreshing)
    ) {
        val filter = selectedTitle.value
        val pictureItems = pictureList.filter { if (filter == "") true else it.fullBreed == filter }
        LazyVerticalStaggeredGrid(
            modifier = Modifier,
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(6.dp),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(count = pictureItems.size, key = { pictureItems[it].imageUrl }) { index ->
                val image = pictureItems[index]
                Box(modifier = Modifier
                    .matchParentSize()
                ) {
                    Picture(modifier = Modifier.align(Alignment.Center), image = image)
                    FavouriteIcon(
                        modifier = Modifier.align(Alignment.TopEnd),
                        image = image,
                        onFavouriteClicked = onFavouriteClicked
                    )
                    ImageTitle(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        image = image,
                        filter = filter,
                        selectedTitle = selectedTitle,
                        showTitle = showTitle
                    )
                }
            }
        }
        Loader(
            modifier = Modifier.align(Alignment.TopCenter),
            pullRefreshState = pullRefreshState,
            refreshing = refreshing
        )
    }
}

@Composable
private fun Picture(modifier: Modifier = Modifier, image: PictureUiItem) {
    GlideImage(
        modifier = modifier,
        imageModel = { image.imageUrl },
        loading = {
            Box(modifier = Modifier.matchParentSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(20.dp)
                )
            }
        },
        failure = {
            Box(modifier = Modifier
                .matchParentSize()
                .background(Color.LightGray.copy(alpha = 0.4f)))
            {
                Text(modifier = Modifier, text = "Image failed to load.")
            }
        },
        requestOptions = {
            RequestOptions
                .timeoutOf(IMAGE_FETCH_TIMEOUT)
                .dontTransform()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        }
    )
}

@Composable
private fun FavouriteIcon(modifier: Modifier, image: PictureUiItem, onFavouriteClicked: (PictureUiItem) -> Unit) {
    val iconColorChecked = if (isSystemInDarkTheme()) LightColors.primary else DarkColors.primary
    Icon(
        modifier = modifier
            .onClick { onFavouriteClicked(image.copy(isFavourite = !image.isFavourite)) },
        imageVector = Icons.Filled.Star,
        contentDescription = "add to favourites: $image",
        tint = if (image.isFavourite)
            iconColorChecked
        else
            MaterialTheme.colors.background.copy(alpha = 0.8f)
    )
}

@Composable
private fun ImageTitle(
    modifier: Modifier,
    image: PictureUiItem,
    filter: String,
    selectedTitle: MutableState<String>,
    showTitle: Boolean
) {
    if (showTitle) {
        Text(
            modifier = modifier
                .padding(6.dp)
                .onClick {
                    selectedTitle.value =
                        if (image.fullBreed == filter) "" else image.fullBreed
                },
            text = image.fullBreed,
            style = MaterialTheme.typography.subtitle1.copy(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
            fontWeight = if (image.fullBreed == filter) FontWeight.ExtraBold else FontWeight.Normal,
            color = Color.White
        )
    }
}
