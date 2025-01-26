package com.hexagraph.cropchain.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagraph.cropchain.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Text(
                "Profile",
                fontSize = 32.sp,
                modifier = Modifier.padding(top = 60.dp, start = 16.dp),
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
            ) {}
            ProfileCard(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 160.dp)
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Other Settings",
            fontSize = 20.sp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(Icons.Default.Build, null, modifier = Modifier.padding(8.dp))
                    Text("Change Wallet", modifier = Modifier.weight(1f))
                    Image(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                        modifier = Modifier.padding(8.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }

            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(Icons.Default.Edit, null, modifier = Modifier.padding(8.dp))
                    Text("Edit Profile", modifier = Modifier.weight(1f))
                    Image(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                        modifier = Modifier.padding(8.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }

            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        Icons.Default.Settings, null,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "Generic Setting",
                        modifier = Modifier.weight(1f)
                    )

                    Image(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                        modifier = Modifier.padding(8.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }

            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .height(60.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        Icons.AutoMirrored.Default.ExitToApp,
                        null,
                        modifier = Modifier.padding(8.dp),
                        colorFilter = ColorFilter.tint(Color.Red)
                    )
                    Text("Log Out", modifier = Modifier.weight(1f), color = Color.Red)
                    Image(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                        modifier = Modifier.padding(8.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }

            }
        }

    }


}

@Composable
fun ProfileCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.farmer),
                modifier = Modifier
                    .padding(16.dp)
                    .clip(CircleShape),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text("Dilip Gogoi", style = MaterialTheme.typography.titleLarge)
                Text("Aadhaar ID: 12345XXXXX", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SettingCard() {

}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    )
}