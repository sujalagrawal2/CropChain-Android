package com.hexagraph.cropchain.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun <T> CarouselViewOfImages(
    modifier: Modifier = Modifier,
    images: List<T>,
    selectedImage: T,
    onImageSelected: (T) -> Unit,
    content: @Composable (image: T, isSelected: Boolean) -> Unit
) {
    val listState = rememberLazyListState()
    val itemSpacing = 16.dp
    val itemWidth = 200.dp // adjust to fit your item
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val center = with(LocalDensity.current) { (screenWidth / 2).toPx() }

    Box(modifier = modifier) {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            contentPadding = PaddingValues(horizontal = (screenWidth - itemWidth) / 2),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(images) { index, item ->
                val itemOffset = remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
                        if (itemInfo != null) {
                            val centerOffset = itemInfo.offset + itemInfo.size / 2
                            val distanceFromCenter = (center - centerOffset).absoluteValue
                            distanceFromCenter / center // Normalize [0..1]
                        } else {
                            1f
                        }
                    }
                }

                val scale by animateFloatAsState(
                    targetValue = lerp(0.85f, 1f, 1f - itemOffset.value.coerceIn(0f, 1f)),
                    label = "scaleAnim"
                )
                val alpha by animateFloatAsState(
                    targetValue = lerp(0.5f, 1f, 1f - itemOffset.value.coerceIn(0f, 1f)),
                    label = "alphaAnim"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                        .clickable { onImageSelected(item) }
                ) {
                    content(item, selectedImage == item)
                }
            }
        }
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (start * (1 - fraction) + stop * fraction)
}