package com.hexagraph.cropchain.ui.screens.onboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.AppButton
import com.hexagraph.cropchain.ui.component.OnboardingTitleSubtitle


@Composable
fun PermissionScreen(modifier: Modifier, viewModel: OnboardingViewModel) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
    ) {
        OnboardingTitleSubtitle(
            largeText = viewModel.visiblePermissionDialogQueue.size.toString() + " Permissions Missing!!",
            smallText = "Grant all permissions to continue"
        )
        Spacer(Modifier.height(16.dp))

        PermissionsRequired.entries.toList().forEach { permissionRequired ->

            AnimatedVisibility(
                viewModel.visiblePermissionDialogQueue.contains(
                    permissionRequired
                )
            ) {
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) {
                    viewModel.checkMissingPermissions(context)
                    if (viewModel.visiblePermissionDialogQueue.isEmpty()) {
                        viewModel.nextButtonAction(context)
                    }
                }
                PermissionCard(
                    permissionsRequired = permissionRequired,
                    isPermanentlyDeclined = isPermissionPermanentlyDeclined(
                        context, permissionRequired.permission
                    ),
                    onOkClick = {
//                        viewModel.dismissDialogue(permissionRequired)
                        launcher.launch(permissionRequired.permission)
                    },
                    onGoToAppSettingsClick = {
//                        viewModel.dismissDialogue(permissionRequired)
                        (context as Activity).openAppSettings()
                    },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}


@Composable
private fun PermissionCard(
    permissionsRequired: PermissionsRequired,
    isPermanentlyDeclined: Boolean,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    painter = painterResource(permissionsRequired.image),
                    contentDescription = "Image",
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        stringResource(permissionsRequired.title),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (!isPermanentlyDeclined)
                            stringResource(permissionsRequired.rationaleText)
                        else
                            stringResource(permissionsRequired.permanentlyDeclinedText),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            AppButton(
                isEnabled = true,
                onClick = if (isPermanentlyDeclined) onGoToAppSettingsClick else onOkClick,
                text = if (isPermanentlyDeclined) stringResource(R.string.button_text_go_to_settings) else stringResource(
                    R.string.button_text_grant
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

private fun isPermissionPermanentlyDeclined(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) != PackageManager.PERMISSION_GRANTED &&
            !ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PermissionScreenPreview() {
    Surface {
        PermissionCard(
            permissionsRequired = PermissionsRequired.NOTIFICATION_PERMISSION,
            isPermanentlyDeclined = true,
            onOkClick = {},
            onGoToAppSettingsClick = {},
        )
    }
}