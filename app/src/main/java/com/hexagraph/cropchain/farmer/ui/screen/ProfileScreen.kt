package com.hexagraph.cropchain.farmer.ui.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.farmer.ui.components.LanguagePreferenceBottomSheet
import com.hexagraph.cropchain.farmer.ui.viewModels.ProfileScreenViewModel
import com.hexagraph.cropchain.ui.component.AppButton
import com.hexagraph.cropchain.ui.theme.cropChainGradient

@Composable
fun ProfileScreen(
    onEditClick: () -> Unit = {},
    onMetamaskClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val ethereumState by viewModel.ethereumState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val balance by viewModel.balance.collectAsState()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val context = LocalContext.current
    val view = LocalView.current
    val window = (view.context as Activity).window

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = cropChainGradient)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, bottom = 24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                    Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
                    .clickable { onEditClick() }
            ){
                Text(
                    text = stringResource(R.string.edit),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.farmer_icon_with_crop),
                    contentDescription = "Farmer icon with crop",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    uiState.currentUserName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp))
                .background(MaterialTheme.colorScheme.background)
                .fillMaxHeight(),
        ) {
            item {
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
                ProfileItem(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.change_your_language_preference),
                    onClick = {
                        viewModel.toggleVisibilityOfLanguagePreferenceBottomSheet()
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Logout
                ProfileItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = stringResource(R.string.log_out),
                    onClick = onLogoutClick
                )
            }
        }
    }

    // Language Preference Bottom Sheet
    if (uiState.isLanguageSelectionBottomSheetVisible) {
        LanguagePreferenceBottomSheet(
            onDismissRequest = { viewModel.toggleVisibilityOfLanguagePreferenceBottomSheet() },
            onChangeSelectedLanguage = { language ->
                viewModel.changeSelectedLanguage(language)
            },
            selectedLanguage = uiState.selectedLanguage,
            onSaveSelectedLanguage = {
                viewModel.saveSelectedLanguage(uiState.selectedLanguage, context)
            }
        )
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
    val borderColor = Color(0xFF2ECC71)
    val balanceShown = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                MaterialTheme.colorScheme.onBackground.copy(0.2f),
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, borderColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.metamask_details),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        MetaMaskInfoRow(label = stringResource(R.string.wallet_address), value = address)
        Spacer(modifier = Modifier.height(8.dp))
        MetaMaskInfoRow(label = stringResource(R.string.chain_id), value = chainId)

        Spacer(modifier = Modifier.height(12.dp))

        AppButton(
            onClick = {
                balanceShown.value = !balanceShown.value
                if (balanceShown.value)
                    onShowBalanceClick()
                else clearBalance()
            },
            modifier = Modifier.fillMaxWidth(),
            isEnabled = true,
            text = if (balanceShown.value) stringResource(R.string.hide_balance)
            else
                stringResource(R.string.show_balance)
        )

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
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 13.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground.copy(0.25f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
    val statusText =
        if (isConnected) stringResource(R.string.connected_to_metamask) else stringResource(
            R.string.not_connected_to_metamask
        )
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
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (!isConnected) {
            AppButton(
                onClick = onConnectClick,
                text = stringResource(R.string.connect_wallet),
                isEnabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
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
            .background(
                MaterialTheme.colorScheme.onBackground.copy(0.15f),
                RoundedCornerShape(12.dp)
            )
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
                Text(title, fontWeight = FontWeight.Medium)
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go",
            )
        }
    }
}
