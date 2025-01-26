package com.hexagraph.cropchain.ui.screens.authentication.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hexagraph.cropchain.ui.screens.authentication.AuthenticationNavigation

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
   Column(modifier = modifier){
        Text( text = "Login Screen", fontSize = 40.sp)
        Button(
            onClick = { navController.navigate(AuthenticationNavigation.MainApp) },
        ) { Text("hello") }
    }
}