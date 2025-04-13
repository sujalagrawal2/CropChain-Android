package com.hexagraph.cropchain.farmer.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagraph.cropchain.R

@Composable
fun HomeScreen(
    onUploadClick: () -> Unit = {},
    onUploadedImagesClick: () -> Unit = {},
    onRecentActivityClick: () -> Unit = {}
) {

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFB58900))
                        .padding(8.dp)
                        .padding(end = 8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
                Text(farmerName, fontSize = 20.sp)
            }
        }
    }
}

