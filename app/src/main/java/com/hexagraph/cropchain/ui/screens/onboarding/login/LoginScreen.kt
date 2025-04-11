package com.hexagraph.cropchain.ui.screens.onboarding.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.AppTextField
import com.hexagraph.cropchain.ui.component.OnboardingTitleSubtitle
import com.hexagraph.cropchain.ui.screens.onboarding.OnboardingUIState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingUIState,
    onAadharChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String)->Unit,
    onDone: () -> Unit,
    ) {
    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OnboardingTitleSubtitle(
            largeText = stringResource(R.string.login_screen_title),
            smallText = stringResource(R.string.login_screen_subtitle),
            modifier = Modifier.padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        AppTextField(
            modifier = Modifier,
            value = uiState.nameQuery,
            onValueChange = onNameChange,
            outerText = "Name",
            placeholderText = "Enter your name",
            icon = Icons.Default.Person,
            isError = false,
            errorText = "Invalid name",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                localFocusManager.moveFocus(FocusDirection.Down)
            })
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppTextField(
            modifier = Modifier,
            value = uiState.aadhaarQuery,
            onValueChange = onAadharChange,
            outerText = stringResource(R.string.aahaar_textfield_outer_text),
            placeholderText = stringResource(R.string.aadhar_textfield_placeholder),
            icon = Icons.Default.CreditCard,
            isError = !uiState.isAadhaarValid,
            errorText = stringResource(R.string.aadhar_text_field_error),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                localFocusManager.moveFocus(FocusDirection.Down)
            })
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppTextField(
            modifier = Modifier,
            value = uiState.password,
            onValueChange = onPasswordChange,
            outerText = stringResource(R.string.password_text_field_outer_text),
            placeholderText = stringResource(R.string.password_text_field_placeholder),
            icon = Icons.Default.Email,
            isError = !uiState.isPasswordValid,
            errorText = stringResource(R.string.password_text_field_error),
            isPassword = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onDone()
            })
        )
    }
}

