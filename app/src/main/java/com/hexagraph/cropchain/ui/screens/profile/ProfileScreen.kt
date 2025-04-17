package com.hexagraph.cropchain.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiStateFlow.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val accounts = listOf(
        "0xCAA2c6ef9fAed6caa3316816a0e511fbcAB4807E",
        "0xa85487b0F672958ceC5553e419ec7a108899c092",
        "0xc78de65857d7eC05F15De58E80AaebB0A68749bc",
        "0x11971094a6227EC40F566495acf1440D851f6C81",
        "0xE37FF49853326588272f6eaE6108D1285e7ff32E",
        "0xd233bf16491bA582274E10a152C5094bf4794ff1",
        "0xdAeafbe1095B5dB8AE793402F6199B87b417DaC7"
    ) // Replace with actual saved accounts
    var selectedAccount by remember { mutableStateOf(accounts.first()) }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Text(
                stringResource(R.string.profile),
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
                uiState = uiState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 160.dp)
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.other_settings),
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
                    .padding(vertical = 4.dp)
            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(Icons.Default.Build, null, modifier = Modifier.padding(8.dp))
//                    Text("Change Wallet", modifier = Modifier.weight(1f))
//                    Image(
//                        Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
//                        modifier = Modifier.padding(8.dp),
//                        colorFilter = ColorFilter.tint(Color.Gray)
//                    )
//                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { expanded = true }
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = selectedAccount, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        accounts.forEach { account ->
                            DropdownMenuItem(
                                text = { Text(account) },
                                onClick = {
                                    selectedAccount = account
                                    expanded = false
                                    viewModel.changeWallet(account)
                                }
                            )
                        }
                    }
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
                    Text(stringResource(R.string.edit_profile), modifier = Modifier.weight(1f))
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
                        text = stringResource(R.string.generic_setting),
                        modifier = Modifier.weight(1f)
                    )

                    Image(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                        modifier = Modifier.padding(8.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }
            }
//            val connectionState by viewModel.connectionState
            val context = LocalContext.current
//            Row {
//                Button(
//                    onClick = { viewModel.connectToBlockChain() }, colors = ButtonColors(
//                        containerColor = if (connectionState == ConnectionState.CONNECTED) Color.Green
//                        else MaterialTheme.colorScheme.primaryContainer,
//                        contentColor = Color.White,
//                        disabledContainerColor = if (connectionState == ConnectionState.CONNECTED) Color.Green
//                        else MaterialTheme.colorScheme.primaryContainer,
//                        disabledContentColor = Color.White,
//                    )
//                ) {
//                    Text("Connect")
//                }
//                if (connectionState == ConnectionState.CONNECTED) {
//                    var iconVisible by remember { mutableStateOf(true) }
//                    if (iconVisible)
//                        Icon(
//                            imageVector = Icons.Filled.Check, // Built-in Material icon
//                            contentDescription = "Favorite Icon", // Accessibility description
//                            tint = Color.Green, // Icon color
//                            modifier = Modifier.size(40.dp)
//
//                        )
//                    Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT)
//                        .show()
//                    CoroutineScope(Dispatchers.IO).launch {
//                        delay(3000)
//                        iconVisible = false
//                    }
//                }
//                if (connectionState == ConnectionState.CONNECTING) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(50.dp),
//                        color = MaterialTheme.colorScheme.primary,
//                        strokeWidth = 4.dp
//                    )
//                }
//                if (connectionState == ConnectionState.ERROR) {
//                    Toast.makeText(context, "Error in Connecting!", Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//            }

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
                    Text(stringResource(R.string.log_out), modifier = Modifier.weight(1f), color = Color.Red)
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
fun ProfileCard(modifier: Modifier = Modifier,
                uiState: ProfileUIState) {
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
                Text(uiState.currentUserName, style = MaterialTheme.typography.titleLarge)
                Text(stringResource(R.string.aadhaar_id, uiState.aadharId), style = MaterialTheme.typography.bodyMedium)
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