package com.hexagraph.cropchain.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavRoutes {

    @Serializable
    data object DashboardScreen : NavRoutes()

    @Serializable
    data object ProfileScreen : NavRoutes()

    @Serializable
    data object UploadScreen : NavRoutes()

    @Serializable
    data object ScientistScreen: NavRoutes()

    @Serializable
    data class FarmerScreen(val address : String): NavRoutes()

    @Serializable
    data object VerifierScreen : NavRoutes()
}