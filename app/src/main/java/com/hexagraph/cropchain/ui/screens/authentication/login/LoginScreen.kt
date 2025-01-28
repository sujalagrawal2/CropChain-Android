package com.hexagraph.cropchain.ui.screens.authentication.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hexagraph.cropchain.ui.component.AppTextField
import com.hexagraph.cropchain.ui.screens.authentication.AuthenticationNavigation

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
//   Column(modifier = modifier){
//        Text( text = "Login Screen", fontSize = 40.sp)
//        Button(
//            onClick = { navController.navigate(AuthenticationNavigation.MainApp) },
//        ) { Text("hello") }
//    }
    //TODO Move these to view model
    var aadhaarNo by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.primary
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .fillMaxHeight(0.77f)
                .clip(shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                .background(
                    MaterialTheme.colorScheme.surface
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    HeadingOfLoginScreen(
                        largeText = "Let's get started",
                        smallText = "Login to get your crop disease diagnosed",
                        modifier = Modifier.padding(top = 30.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
                item {
                    AppTextField(
                        modifier = Modifier,
                        value = aadhaarNo,
                        onValueChange = { aadhaarNo = it },
                        outerText = "Login with Aadhaar",
                        placeholderText = "Enter your Aadhaar Number",
                        icon = Icons.Default.CreditCard,
                        isError = false,//TODO
                        errorText = "",//TODO
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
                item {
                    AppTextField(
                        modifier = Modifier,
                        value = password,
                        onValueChange = { password = it },
                        outerText = "Password",
                        placeholderText = "Enter your password",
                        icon = Icons.Default.Email,
                        isError = false,//TODO
                        errorText = "",//TODO,
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
                item{
                    Button(
                        onClick = {
                            navController.navigate(AuthenticationNavigation.MainApp)
                        },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {

                    }
                }
            }
        }
    }
}

@Composable
fun HeadingOfLoginScreen(
    modifier: Modifier = Modifier,
    largeText: String, smallText: String) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = largeText,

            // heading large 24
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight(600),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = smallText,

            // content 16
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            )
        )
    }
}