package com.hexagraph.cropchain.ui.component

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String = "Continue",
    isEnabled: Boolean,
    onClick: () -> Unit = {}
) {
    Button(onClick = { if(isEnabled) onClick()}, enabled = isEnabled, modifier = modifier.widthIn(min = 80.dp)) {
        Text(text = text)
    }
}