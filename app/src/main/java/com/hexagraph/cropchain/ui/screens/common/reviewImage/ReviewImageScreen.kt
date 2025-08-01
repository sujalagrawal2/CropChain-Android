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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun ReviewImageScreen(
    imageUrl: String,
    type: Int,
    viewModel: ReviewImageScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getInfo(imageUrl, type)
    }
    val uiState = viewModel.uiState
    val images = uiState.value.images
    val pagerState = rememberPagerState(pageCount = { images.size })
    val context = LocalContext.current
    if (uiState.value.error != null) {
        Toast.makeText(context, uiState.value.error, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0F0C))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = if (type == 1) "Review Image" else "Verify Image",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box {
                HorizontalPager(state = pagerState) { page ->

                    val url = "https://orange-many-shrimp-59.mypinata.cloud/ipfs/${images[page]}"
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = "Image $page",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(16.dp)
                        .background(Color.White, CircleShape)
                )
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

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "AI Review",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = uiState.value.aiSolution,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (type == 1) "Write Your Review" else "Review",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (type != 1)
                Text(
                    text = uiState.value.reviewText,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
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
            if (type != 1) {
                Row {
                    IconButton(onClick = { viewModel.onLikeClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Like",
                            tint = if (uiState.value.liked == true) Color.Green else Color.White
                        )
                    }
                    IconButton(onClick = { viewModel.onDislikeClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = "Dislike",
                            tint = if (uiState.value.liked == false) Color.Red else Color.White
                        )
                    }
                }
                Row {
                    Text(
                        text = "${uiState.value.likeCount} Likes",
                        color = Color.White,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "${uiState.value.dislikeCount} Dislikes",
                        color = Color.White
                    )
                }
            }


            if (type != 0)
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
                    Text(if (type == 1) "Submit Review" else "Submit Choice")
                }
        }
    }
}
