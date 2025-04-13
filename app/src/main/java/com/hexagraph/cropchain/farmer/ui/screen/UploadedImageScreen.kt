package com.hexagraph.cropchain.farmer.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.farmer.ui.viewModels.UploadedImageUIState
import com.hexagraph.cropchain.farmer.ui.viewModels.UploadedImageViewModel

@Composable
fun UploadedImageScreen(
    onBackButtonPressed: () -> Unit,
    viewModel: UploadedImageViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf("Verified") }
    val tabs = listOf("Verified", "Pending")
    val backgroundColor = Color(0xFF000000)
    val cardColor = Color(0xFF1E1E1E)
    val accentColor = Color(0xFF4CAF50)
    val textColor = Color.White
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackButtonPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = textColor
                )
            }
            Text(
                text = "Uploaded Images",
                style = MaterialTheme.typography.headlineSmall.copy(color = textColor),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val tabBackground = Color(0xFF1E1E1E)
        val selectedTabColor = Color(0xFF66BB6A) // Soft green, more pastel
        val unselectedTabColor = Color(0xFF888888)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = tabBackground),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

        if (selectedTab == "Verified") {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(uiState.value.verifiedImages) { uri ->
                    Log.d("Uploaded Image Screen", uri)
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
        else{
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(uiState.value.pendingImages) { uri ->
                    Log.d("Uploaded Image Screen", uri)
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
//        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            items(10) { index ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(containerColor = cardColor),
//                    shape = RoundedCornerShape(16.dp),
//                    elevation = CardDefaults.cardElevation(6.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(60.dp)
//                                .clip(RoundedCornerShape(12.dp))
//                                .background(Color.Gray.copy(alpha = 0.4f)),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Image,
//                                contentDescription = "Image",
//                                tint = Color.LightGray,
//                                modifier = Modifier.size(30.dp)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                text = "crop_image_$index.jpg",
//                                color = textColor,
//                                style = MaterialTheme.typography.bodyLarge,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                            Text(
//                                text = if (selectedTab == "Verified") "Uploaded to Blockchain"
//                                else "Waiting for Upload",
//                                color = Color.Gray,
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//
//                        Icon(
//                            imageVector = if (selectedTab == "Verified") Icons.Default.CheckCircle else Icons.Default.HourglassEmpty,
//                            contentDescription = null,
//                            tint = if (selectedTab == "Verified") accentColor else Color.Gray,
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//                }
//            }
//        }
    }
}

