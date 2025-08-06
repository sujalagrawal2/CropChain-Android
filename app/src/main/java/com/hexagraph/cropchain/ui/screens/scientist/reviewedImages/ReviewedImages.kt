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
import com.hexagraph.cropchain.ui.screens.farmer.uploadedImages.CropItem
import com.hexagraph.cropchain.ui.screens.scientist.home.HomeScreenViewModel

@Composable
fun ReviewedImages(
    type: Int,
    onImageClicked: (String, Int) -> Unit,
    viewModel: ReviewedImagesViewModel = hiltViewModel()
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

        val imageSections =
            if (type == 1 ) uiState.value.reviewImages
            else uiState.value.verifyImages


        val imagesUrl: MutableList<CropItem> = emptyList<CropItem>().toMutableList()
        for (images in imageSections) {
            val splitImages = images.url.split("$").filter { it.isNotBlank() }
            imagesUrl.add(images.copy(url = splitImages[0]))
        }

        DisplayImageList(
            imagesUrl,
            onClickImage = {
                onImageClicked(
                    imageSections[it].id.toString(),
                   0
                )
            })

    }


}
