package com.hexagraph.cropchain.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTextField(
    modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit,
    outerText: String = "Login with email",
    placeholderText: String = "Enter your email",
    icon: ImageVector? = Icons.Default.Email,
    isError: Boolean = false,
    errorText: String = "Invalid Email Format",
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    Box(
        modifier = modifier.width(314.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = outerText,
                // content 16 bold
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                ),
                modifier = Modifier.height(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginTextField(
                modifier =
                Modifier.fillMaxWidth(),
                placeholderText = placeholderText,
                value = value,
                icon = icon,
                isError = isError,
                errorText = errorText,
                keyboardOptions = keyboardOptions,
                isPassword = isPassword
            ) {
                onValueChange(it)
            }
        }
    }
}

@Composable
fun LoginTextField(
    modifier: Modifier, placeholderText: String,
    icon: ImageVector? = Icons.Default.Email,
    value: String, isError: Boolean,
    isPassword: Boolean, keyboardOptions: KeyboardOptions,
    errorText: String, onValueChange: (String) -> Unit,
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(value = value, onValueChange = {
        onValueChange(it)
    }, placeholder = {
        Text(
            text = placeholderText,
            // content 16
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight(400),
//                color = Color(0xFFADADAD),
            )
        )
    },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        isError = isError,
        supportingText = { if (isError) Text(text = errorText) },
        leadingIcon = {
            if(icon != null)
                Icon(
                    imageVector = icon, contentDescription = null,
//                tint = Color(0xfffb8a7a),
                    modifier = Modifier
                        .width(20.dp)
                        .height(16.dp)
                )

        },
        trailingIcon = {
            if (isPassword)
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector =
                        if (showPassword) Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
        },
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = Color(0x99AED5FF),
//            unfocusedContainerColor = Color(0x99AED5FF),
//            errorContainerColor = Color(0x99AED5FF),
//            disabledContainerColor = Color(0x99AED5FF),
//            unfocusedIndicatorColor = Color(0x99939FAC),
//            focusedIndicatorColor = colorResource(id = R.color.colorPrimary),
//            errorIndicatorColor = Color(194, 27, 33, 128)
//        ),
//        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
}