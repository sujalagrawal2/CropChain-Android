package com.hexagraph.cropchain.farmer.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.farmer.ui.viewModels.HomeScreenUIState
import com.hexagraph.cropchain.farmer.ui.viewModels.HomeScreenViewModel
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.URI

@Composable
fun HomeScreen(
    onUploadClick: () -> Unit = {},
    onUploadedImagesClick: () -> Unit = {},
    onRecentActivityClick: () -> Unit = {},
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        DashboardScreenTitle()
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onUploadClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp), // Increased height
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Upload an Image",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Get reviews from scientists",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Pets, contentDescription = null, tint = Color(0xFFFF6D00))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Uploaded Images", color = Color.White, fontWeight = FontWeight.Bold)
            }

            IconButton(onClick = onUploadedImagesClick) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Go",
                    tint = Color.White
                )
            }
        }

        CropImageGallery(viewModel.uiState.value)

//        val scrollState = rememberScrollState()
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .horizontalScroll(scrollState)
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            uiState.value.uploadedImages.forEach { crop ->
//                var url = crop.url
//                if (isLocalImageExists(crop.uid)) url = crop.uid
//
//                Box(
//                    modifier = Modifier
//                        .size(160.dp)
//                        .clip(RoundedCornerShape(20.dp))
//                        .background(Color(0xFF1A1A1A))
//                        .shadow(10.dp, RoundedCornerShape(20.dp))
//                        .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
//                        .clickable {
//                            Log.d("ImageGallery", "Clicked UID: ${crop.uid}")
//                        }
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(
//                            model = url,
//                        ),
//                        contentDescription = "Crop Image",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clip(RoundedCornerShape(20.dp))
//                    )
//                }
//            }
//        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = Color(0xFFFF6D00))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Recent Activity", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
                .clickable { onRecentActivityClick() }
                .padding(16.dp)
        ) {
            Column {
                Text("An image was verified!", color = Color.White, fontWeight = FontWeight.Medium)
                Text("Tap to see review", color = Color.LightGray, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DashboardScreenTitle(
    modifier: Modifier = Modifier,
    farmerName: String = "Dilip Gogoi",

    ) {
    Box(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.farmer_icon_with_crop),
                contentDescription = "Dashboard Icon",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 8.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    "Hello",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Text(farmerName, fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun CropImageGallery(uiState: HomeScreenUIState) {
    val scrollState = rememberScrollState()

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column {
        // — thumbnail row, aligned under the header —
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            uiState.uploadedImages.forEachIndexed { index, crop ->
                val url = if (isLocalImageExists(crop.uid)) crop.uid else crop.url

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1A1A1A))
                ) {
                    // thumbnail
                    Image(
                        painter = rememberAsyncImagePainter(model = url),
                        contentDescription = "Crop Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // expand icon
                    IconButton(
                        onClick = { selectedIndex = index },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Expand",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // — full‑screen swipeable viewer —
        selectedIndex?.let { initialPage ->
            Dialog(
                onDismissRequest = { selectedIndex = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                val pagerState = rememberPagerState(
                    initialPage = initialPage,
                    initialPageOffsetFraction = 0f,
                    pageCount = { uiState.uploadedImages.size }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val crop = uiState.uploadedImages[page]
                        val url = if (isLocalImageExists(crop.uid)) crop.uid else crop.url

                        Image(
                            painter = rememberAsyncImagePainter(model = url),
                            contentDescription = "Full Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // close button
                    IconButton(
                        onClick = { selectedIndex = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
            }
        }
    }
}


fun isLocalImageExists(imagePath: String): Boolean {
    return try {
        val file = File(URI(imagePath))
        file.exists()
    } catch (e: Exception) {
        false
    }
}

