package com.hexagraph.cropchain.farmer.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.farmer.ui.viewModels.ProfileScreenViewModel
import com.hexagraph.cropchain.R

@Composable
fun ProfileScreen(
    userName: String = "Dilip Gogoi",
    onEditClick: () -> Unit = {},
    onMetamaskClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val ethereumState by viewModel.ethereumState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val balance by viewModel.balance.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn() {
            item {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2ECC71))
                        .padding(top = 32.dp, bottom = 24.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    TextButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .height(28.dp)
                    ) {
                        Text("Edit", fontSize = 12.sp, color = Color.Black)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            userName,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                // Metamask Connection Status
                ConnectionStatusCard(isConnected, onConnectClick = { viewModel.connect() })

                Spacer(modifier = Modifier.height(24.dp))
                if (isConnected && ethereumState != null) {
                    MetaMaskInfoCard(
                        address = ethereumState!!.selectedAddress,
                        chainId = ethereumState!!.chainId,
                        balance = balance,
                        onShowBalanceClick = {
                            viewModel.fetchBalance()
                        },
                        clearBalance = {
                            viewModel.clearBalance()
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Metamask Accounts Section
                repeat(3) {
                    ProfileItem(
                        icon = Icons.Default.Pets,
                        title = stringResource(R.string.metamask_accounts),
                        onClick = onMetamaskClick
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout
                ProfileItem(
                    icon = Icons.Default.Pets,
                    title = stringResource(R.string.log_out),
                    onClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
fun MetaMaskInfoCard(
    address: String,
    chainId: String,
    balance: String?,
    onShowBalanceClick: () -> Unit,
    clearBalance: () -> Unit
) {
    val cardBgColor = Color(0xFF1E1E1E)
    val borderColor = Color(0xFF2ECC71)
    val balanceShown = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(cardBgColor, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text =stringResource(R.string.metamask_details),
            color = borderColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        MetaMaskInfoRow(label = stringResource(R.string.wallet_address), value = address)
        Spacer(modifier = Modifier.height(8.dp))
        MetaMaskInfoRow(label =stringResource(R.string.chain_id), value = chainId)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                balanceShown.value = !balanceShown.value
                if (balanceShown.value)
                    onShowBalanceClick()
                else clearBalance()
            },
            colors = ButtonDefaults.buttonColors(containerColor = borderColor),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (balanceShown.value)
                Text(stringResource(R.string.hide_balance), color = Color.Black)
            else
                Text(stringResource(R.string.show_balance), color = Color.Black)
        }
        if (balanceShown.value)
            balance?.let {
                Spacer(modifier = Modifier.height(12.dp))
                MetaMaskInfoRow(label = stringResource(R.string.balance), value = it)
            }
    }
}


@Composable
fun MetaMaskInfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 13.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = value,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun ConnectionStatusCard(
    isConnected: Boolean,
    onConnectClick: () -> Unit = {}
) {
    val bgColor =
        if (isConnected) Color(0xFF2ECC71).copy(alpha = 0.15f) else Color(0xFFB53737).copy(alpha = 0.15f)
    val borderColor = if (isConnected) Color(0xFF2ECC71) else Color(0xFFB53737)
    val icon = if (isConnected) Icons.Default.Check else Icons.Default.Warning
    val statusText = if (isConnected) "Connected to MetaMask" else "Not Connected to MetaMask"
    val iconTint = if (isConnected) Color(0xFF2ECC71) else Color(0xFFFF6D00)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = statusText,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (!isConnected) {
            Button(
                onClick = onConnectClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.connect_wallet), color = Color.Black)
            }
        }
    }
}


@Composable
fun ProfileItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.DarkGray, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFFFF6D00))
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.Medium)
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Go",
                tint = Color.White
            )
        }
    }
}
