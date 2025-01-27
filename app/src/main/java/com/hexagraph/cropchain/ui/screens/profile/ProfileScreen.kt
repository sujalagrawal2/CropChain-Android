package com.hexagraph.cropchain.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.ConnectionState
import com.hexagraph.cropchain.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
) {
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
            val connectionState by viewModel.connectionState
            val context = LocalContext.current
            Row {
                Button(
                    onClick = { viewModel.connectToBlockChain() }, colors = ButtonColors(
                        containerColor = if (connectionState == ConnectionState.CONNECTED) Color.Green
                        else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = Color.White,
                        disabledContainerColor = if (connectionState == ConnectionState.CONNECTED) Color.Green
                        else MaterialTheme.colorScheme.primaryContainer,
                        disabledContentColor = Color.White,
                    )
                ) {
                    Text("Connect")
                }
                if (connectionState == ConnectionState.CONNECTED) {
                    var iconVisible by remember { mutableStateOf(true) }
                    if (iconVisible)
                        Icon(
                            imageVector = Icons.Filled.Check, // Built-in Material icon
                            contentDescription = "Favorite Icon", // Accessibility description
                            tint = Color.Green, // Icon color
                            modifier = Modifier.size(40.dp)

                        )
                    Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT)
                        .show()
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(3000)
                        iconVisible = false
                    }
                }
                if (connectionState == ConnectionState.CONNECTING) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
                if (connectionState == ConnectionState.ERROR) {
                    Toast.makeText(context, "Error in Connecting!", Toast.LENGTH_SHORT)
                        .show()
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

//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen(
//        Modifier
//            .padding(8.dp)
//            .fillMaxSize()
//    )
//}