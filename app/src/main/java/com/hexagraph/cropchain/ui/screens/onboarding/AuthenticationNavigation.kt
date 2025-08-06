package com.hexagraph.cropchain.ui.screens.onboarding

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AuthenticationNavigation: NavKey {

    @Serializable
    data class OnBoarding(
        val allowedScreens: List<OnboardingScreens> = OnboardingScreens.entries,
        val timestamp: Long = System.currentTimeMillis()
    ) : AuthenticationNavigation

    @Serializable
    data object MainApp: AuthenticationNavigation
}

