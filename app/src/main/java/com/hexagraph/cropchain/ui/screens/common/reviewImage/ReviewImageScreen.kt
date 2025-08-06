package com.hexagraph.cropchain.ui.screens.common.reviewImage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.screens.common.reviewImage.ReviewScreenType.FARMER
import com.hexagraph.cropchain.ui.screens.common.reviewImage.ReviewScreenType.REVIEW
import com.hexagraph.cropchain.ui.screens.common.reviewImage.ReviewScreenType.VERIFY

enum class ReviewScreenType() {
    FARMER,
    REVIEW,
    VERIFY,
    PREVIEW
}

@Composable
fun ReviewImageScreen(
    id: Int,
    type: Int,
    viewModel: ReviewImageScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getInfo(id, type)
    }
    val uiState = viewModel.uiState
    val images = uiState.value.images
    val pagerState = rememberPagerState(pageCount = { images.size })
    val context = LocalContext.current

    var showFullScreenImage by remember { mutableStateOf(false) }
    var initialFullScreenPage by remember { mutableIntStateOf(0) }

    if (uiState.value.error != null) {
        Toast.makeText(context, uiState.value.error, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }

    val screenType = ReviewScreenType.entries[type]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0F0C))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = when (screenType) { // Used renamed variable
                    FARMER -> "Farmer"
                    REVIEW -> "Review Image"
                    VERIFY -> "Verify Image"
                    else -> "Preview Image"
                },
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (images.isNotEmpty()) {
                Box {
                    HorizontalPager(state = pagerState) { page ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            val url = images[page]
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = url,
                                    placeholder = painterResource(id = R.drawable.placeholder_image),
                                    error = painterResource(id = R.drawable.placeholder_image)
                                ),
                                contentDescription = "Image $page",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = {
                                    initialFullScreenPage = page
                                    showFullScreenImage = true
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "View Fullscreen",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else Color.Gray)
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "Title",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = uiState.value.title,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Description",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = uiState.value.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (screenType != FARMER) { // Used renamed variable
                Text(
                    "AI Review",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = uiState.value.aiSolution,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            Text(
                text = if (screenType == REVIEW) "Write Your Review" else "Review", // Used renamed variable
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (screenType != REVIEW) // Used renamed variable
                Text(
                    text = uiState.value.reviewText,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                )
            else {

                OutlinedTextField(
                    value = uiState.value.reviewText,
                    onValueChange = { viewModel.onReviewTextChanged(it) },
                    placeholder = { Text("Type your feedback here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(top = 8.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White
                    )
                )
            }



            Spacer(modifier = Modifier.height(12.dp))
            if (screenType != REVIEW && screenType != FARMER) { // Used renamed variable
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Like",
                            tint = if (uiState.value.liked == true) Color.Green else Color.White
                        )
                        Text(
                            text = "${uiState.value.likeCount}",
                            color = Color.White,
//                          modifier = Modifier.padding(end = 16.dp)
                        )
                    }


                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = "Dislike",
                            tint = if (uiState.value.liked == false) Color.Red else Color.White
                        )
                        Text(
                            text = "${uiState.value.dislikeCount}",
                            color = Color.White
                        )
                    }
                }
            }


            if (screenType == REVIEW) { // Used renamed variable
                Button(
                    onClick = {
                        viewModel.submit()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007E33),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text("Submit Review")
                }
            } else if (screenType == VERIFY) { // Used renamed variable
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.onApproveClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007E33),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text("Approve")
                    }
                    Button(
                        onClick = {
                            viewModel.onDisapproveClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007E33), // Consider a different color for Disapprove
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text("Disapprove")
                    }

                }
            }
        }
    }

    if (showFullScreenImage && images.isNotEmpty()) {
        Dialog(
            onDismissRequest = { showFullScreenImage = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val fullScreenPagerState = rememberPagerState(
                initialPage = initialFullScreenPage,
                pageCount = { images.size }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                HorizontalPager(
                    state = fullScreenPagerState,
                    modifier = Modifier
                        .fillMaxSize()
                ) { pageIndex ->
                    val imageUrlDialog = images[pageIndex]
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrlDialog),
                        contentDescription = "Full Screen Image $pageIndex",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${fullScreenPagerState.currentPage + 1} / ${images.size}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(
                    onClick = { showFullScreenImage = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Fullscreen View",
                        tint = Color.White
                    )
                }
            }
        }
    }

    // Confirmation Dialog for Approve/Disapprove
    if (uiState.value.showConfirmationDialog) {
        Dialog(onDismissRequest = { viewModel.onConfirmationResult(false) }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (uiState.value.pendingAction) {
                            PendingAction.APPROVE -> "Are you sure you want to approve?"
                            PendingAction.DISAPPROVE -> "Are you sure you want to disapprove?"
                            else -> "Are you sure?"
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { viewModel.onConfirmationResult(true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Yes")
                        }
                        Button(
                            onClick = { viewModel.onConfirmationResult(false) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text("No")
                        }
                    }
                }
            }
        }
    }
}
