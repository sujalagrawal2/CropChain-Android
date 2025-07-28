package com.hexagraph.cropchain.ui.screens.farmer.uploadedImage

import android.R.attr.scaleX
import android.R.attr.scaleY
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.R

//@Composable
//fun UploadedImageScreen(
//    onBackButtonPressed: () -> Unit,
//    viewModel: UploadedImageViewModel = hiltViewModel()
//) {
//    var selectedTab by remember { mutableStateOf("Verified") }
//    val tabs = listOf("Verified", "Pending")
//    val uiState = viewModel.uiState
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Top Bar
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            IconButton(onClick = onBackButtonPressed) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBackIosNew,
//                    contentDescription = null,
//                )
//            }
//            Text(
//                text = stringResource(R.string.uploaded_images ),
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(start = 8.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        val tabBackground = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
//        val selectedTabColor = MaterialTheme.colorScheme.primary// Soft green, more pastel
//        val unselectedTabColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp),
//            colors = CardDefaults.cardColors(containerColor = tabBackground),
//            shape = RoundedCornerShape(20.dp),
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                tabs.forEach { tab ->
//                    val isSelected = tab == selectedTab
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(
//                                if (isSelected) selectedTabColor.copy(alpha = 0.2f) else Color.Transparent
//                            )
//                            .clickable { selectedTab = tab }
//                            .padding(vertical = 10.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = tab,
//                            color = if (isSelected) selectedTabColor else unselectedTabColor,
//                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
//                        )
//                    }
//                }
//            }
//        }
//
//
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        if (selectedTab == "Verified") {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(8.dp)
//            ) {
//                items(uiState.value.verifiedImages) { uri ->
//                    Log.d("Uploaded Image Screen", uri)
//                    val url = "https://orange-many-shrimp-59.mypinata.cloud/ipfs/$uri"
//                    Image(
//                        painter = rememberAsyncImagePainter(url),
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .aspectRatio(1f)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }
//            }
//        }
//        else{
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(8.dp)
//            ) {
//                items(uiState.value.pendingImages) { uri ->
//                    Log.d("Uploaded Image Screen", uri)
//                    val url = "https://orange-many-shrimp-59.mypinata.cloud/ipfs/$uri"
//                    Image(
//                        painter = rememberAsyncImagePainter(url),
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .aspectRatio(1f)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                }
//            }
//        }
//
//    }
//}

@Composable
fun UploadedImageScreen(
    onBackButtonPressed: () -> Unit,
    viewModel: UploadedImageViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf("Verified") }
    val tabs = listOf("Verified", "Pending")
    val uiState = viewModel.uiState

    val imageSections =
        if (selectedTab == "Verified") uiState.value.verifiedImages
        else uiState.value.pendingImages

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                text = stringResource(R.string.uploaded_images),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        val tabBackground = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
        val selectedTabColor = MaterialTheme.colorScheme.primary
        val unselectedTabColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = tabBackground),
            shape = RoundedCornerShape(20.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == selectedTab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) selectedTabColor.copy(alpha = 0.2f) else Color.Transparent
                            )
                            .clickable { selectedTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) selectedTabColor else unselectedTabColor,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(imageSections) { section ->
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    val urls = section.split("$").filter { it.isNotBlank() }
                    ImageSectionCarousel(urls)
//                        LazyRow(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(12.dp),
//                            horizontalArrangement = Arrangement.spacedBy(12.dp),
//                            contentPadding = PaddingValues(horizontal = 8.dp)
//
//                        ) {
//
//                            items(urls) { uri ->
//                                val url = "https://orange-many-shrimp-59.mypinata.cloud/ipfs/$uri"
//                                Image(
//                                    painter = rememberAsyncImagePainter(url),
//                                    contentDescription = null,
//                                    contentScale = ContentScale.Crop,
//                                    modifier = Modifier
//                                        .size(120.dp)
//                                        .clip(RoundedCornerShape(12.dp))
//                                )
//                            }
//                        }

                }
            }
        }
    }
}

@Composable
fun ImageSectionCarousel(section: List<String>) {
    val listState = rememberLazyListState()

    val centerItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportEndOffset / 2

            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                kotlin.math.abs(itemCenter - viewportCenter)
            }?.index ?: 0
        }
    }

    // Width of image + spacing → used to calculate side padding
    val itemSize = 200.dp
    val itemSpacing = 24.dp
    val screenPadding = with(LocalDensity.current) { (itemSize / 2).roundToPx() }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            contentPadding = PaddingValues(horizontal = itemSize / 2) // key part here!
        ) {
            itemsIndexed(section) { index, uri ->
                val url = "https://orange-many-shrimp-59.mypinata.cloud/ipfs/$uri"
                val isFocused = index == centerItemIndex

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = if (isFocused) -20f else 0f
                            scaleX = if (isFocused) 1.1f else 1f
                            scaleY = if (isFocused) 1.1f else 1f
                        }
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(itemSize)
                            .clip(RoundedCornerShape(12.dp))
                    )

//                    if (!isFocused) {
//                        Box(
//                            modifier = Modifier
//                                .matchParentSize()
//                                .background(Color.Black.copy(alpha = 0.4f))
//                        )
//                    }
                }
            }
        }
    }
}




