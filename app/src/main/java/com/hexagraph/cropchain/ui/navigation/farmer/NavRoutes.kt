package com.hexagraph.cropchain.ui.navigation.farmer

sealed class NavRoutes(val route: String) {
    object HomeScreen : NavRoutes("home")
    object SelectImageScreen : NavRoutes("select_image_screen")
    object ProfileScreen : NavRoutes("profile_screen")
    object UploadStatusScreen : NavRoutes("upload_status_screen")
    object UploadIImageScreen : NavRoutes("upload_image_screen")
    object UploadedImageScreen : NavRoutes("uploaded_image_screen")
}