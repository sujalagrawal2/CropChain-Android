package com.hexagraph.cropchain.ui.screens.farmer.uploadedImages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.DisplayImageList

@Composable
fun UploadedImagesScreen(
    selectedTab: String,
    onBackButtonPressed: () -> Unit,
    viewModel: UploadedImagesViewModel = hiltViewModel(),
    onImageSelected: (String, Int) -> Unit
) {
    val uiState = viewModel.uiState
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Top Bar
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackButtonPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                    )
                }
                Text(
                    text = if (selectedTab == "Verified") "Verified Images" else "Pending Images",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val imageSections =
                if (selectedTab == "Verified") uiState.value.verifiedImages
                else uiState.value.pendingImages

            val imagesUrl: MutableList<CropItem> = emptyList<CropItem>().toMutableList()
            for (images in imageSections) {
                val splitImages = images.url.split("$").filter { it.isNotBlank() }
                imagesUrl.add(images.copy(url = splitImages[0]))
            }
            DisplayImageList(
                imagesUrl,
                onClickImage = {
                    onImageSelected(
                        imageSections[it].id.toString(),
                        0
                    )
                })
        }
    }
}
