package com.hexagraph.cropchain.ui.navigation.farmer


sealed class NavRoutes(val route: String) {
    object HomeScreen : NavRoutes("home")
    object SelectImageScreen : NavRoutes("select_image_screen")
    object ProfileScreen : NavRoutes("profile_screen")
    object UploadStatusScreen : NavRoutes("upload_status_screen")
    object UploadIImageScreen : NavRoutes("upload_image_screen")
    object UploadedImageScreen : NavRoutes("uploaded_image_screen")


    object ReviewImageScreen : NavRoutes("review_image_screen/{id}/{type}") {
        fun passArgs(id: String, type: Int): String {
            return "review_image_screen/$id/$type"
        }
    }
}