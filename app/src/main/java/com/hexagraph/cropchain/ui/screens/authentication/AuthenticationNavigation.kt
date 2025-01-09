package com.hexagraph.cropchain.ui.screens.authentication

import kotlinx.serialization.Serializable

sealed interface AuthenticationNavigation {

    @Serializable
    data object LoginScreen : AuthenticationNavigation

}