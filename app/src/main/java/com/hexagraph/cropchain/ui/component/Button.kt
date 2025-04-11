package com.hexagraph.cropchain.ui.component

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hexagraph.cropchain.R

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.button_text_continue),
    isEnabled: Boolean,
    onClick: () -> Unit = {}
) {
    Button(onClick = { if(isEnabled) onClick()},
        enabled = isEnabled,
        modifier = modifier
            .widthIn(min = 80.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                shape = RoundedCornerShape(15.dp)
            )
            .shadow(
                elevation = 14.dp,
                spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        ,
        shape = RoundedCornerShape(8.dp)
        ) {
        Text(text = text,
            fontWeight = FontWeight.Bold)
    }
}