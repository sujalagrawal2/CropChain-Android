package com.hexagraph.cropchain.ui.screens.scientist.reviewedImages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.ui.component.DisplayImageList
import com.hexagraph.cropchain.ui.screens.scientist.home.HomeScreenViewModel

@Composable
fun ReviewedImages(
    type: Int,
    onImageClicked: (String, Int) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                )
            }
            Text(
                text = if (type == 1) "Reviewed Images" else "Verified Images",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        val reviewedImages: MutableList<String> = emptyList<String>().toMutableList()
        for (i in uiState.value.reviewedImages) reviewedImages.add(i[0])

        val verifiedImages: MutableList<String> = emptyList<String>().toMutableList()
        for (i in uiState.value.verifiedImages) verifiedImages.add(i[0])

        if (type == 1) DisplayImageList(reviewedImages, onClickImage = {
            onImageClicked(uiState.value.reviewedImagesOriginal[it], 0)
        })
        else DisplayImageList(
            verifiedImages,
            onClickImage = { onImageClicked(uiState.value.verifiedImagesOriginal[it], 0) })

    }


}
