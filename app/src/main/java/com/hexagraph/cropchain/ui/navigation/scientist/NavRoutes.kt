package com.hexagraph.cropchain.ui.navigation.scientist

sealed class NavRoutes(val route: String) {
    object HomeScreen : NavRoutes("home")
    object ProfileScreen : NavRoutes("profile_screen")
    object ReviewScreen : NavRoutes("review_screen")

    object ReviewImageScreen : NavRoutes("review_image_screen/{url}/{type}") {
        fun passArgs(url: String, type: Int): String {
            return "review_image_screen/$url/$type"
        }
    }
    object ReviewedImageScreen : NavRoutes("reviewed_image_screen/{type}") {
        fun passArgs(type: Int): String {
            return "reviewed_image_screen/$type"
        }
    }
}