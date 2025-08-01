package com.hexagraph.cropchain.ui.screens.scientist.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pets
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.theme.cropChainGradient
import com.hexagraph.cropchain.ui.theme.cropChainOrange

@Composable
fun HomeScreen(
    onReviewedImagesClicked: () -> Unit = {},
    onRecentActivityClick: () -> Unit = {},
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onVerifiedImagesClicked: () -> Unit = {}
) {


    val uiState = viewModel.uiState
    val context = LocalContext.current
    if (uiState.value.error != null) {
        Toast.makeText(context, uiState.value.error, Toast.LENGTH_SHORT).show()
        viewModel.toastMessageShown()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        DashboardScreenTitle(farmerName = uiState.value.userName)
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(brush = cropChainGradient),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.upload_image),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Text(
                        text = stringResource(R.string.get_reveiw_from_scientist),
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
                Text("Reviewed Images", fontWeight = FontWeight.Bold)
            }

            IconButton(onClick = {
                onReviewedImagesClicked()

            }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go",
                )
            }
        }

        val reviewedImages: MutableList<String> = emptyList<String>().toMutableList()
        for (i in uiState.value.reviewedImages) reviewedImages.add(i[0])
        CropImageGallery(reviewedImages)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Pets, contentDescription = null, tint = Color(0xFFFF6D00))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Verified Images", fontWeight = FontWeight.Bold)
            }

            IconButton(onClick = {
                onVerifiedImagesClicked()

            }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go",
                )
            }
        }

        val verifiedImages: MutableList<String> = emptyList<String>().toMutableList()
        for (i in uiState.value.verifiedImages) verifiedImages.add(i[0])
        CropImageGallery(verifiedImages)

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = cropChainOrange)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.recent_activity), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onBackground.copy(0.15f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onRecentActivityClick() }
                .padding(16.dp)
        ) {
            Column {
                Text(stringResource(R.string.an_image_was_verified), fontWeight = FontWeight.Medium)
                Text(stringResource(R.string.tap_to_see_review), fontSize = 12.sp)
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
                    stringResource(R.string.hello),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
                Text(farmerName, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun CropImageGallery(images: List<String>) {
    val scrollState = rememberScrollState()

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            images.forEachIndexed { index, crop ->
                val url = "https://orange-many-shrimp-59.mypinata.cloud/ipfs/$crop"
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
                            .background(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
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
                    pageCount = { images.size }
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
                        val crop = images[page]
                        val url = "https://orange-many-shrimp-59.mypinata.cloud/ipfs/$crop"
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

