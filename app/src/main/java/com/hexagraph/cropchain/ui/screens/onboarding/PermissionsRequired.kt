package com.hexagraph.cropchain.ui.screens.onboarding

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.hexagraph.cropchain.R

enum class PermissionsRequired(
    val permission: String,
    val title: Int,
    val permanentlyDeclinedText: Int,
    val rationaleText: Int,
    val image: Int,
    val minSDK: Int
) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    NOTIFICATION_PERMISSION(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        title = R.string.permission_title_notification,
        permanentlyDeclinedText = R.string.permanently_declined_rationale,
        rationaleText = R.string.notification_permission_rationale_text,
        image = R.drawable.baseline_notifications_active_24,
        minSDK = 24
    )

}