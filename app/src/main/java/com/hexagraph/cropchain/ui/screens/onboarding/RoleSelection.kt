package com.hexagraph.cropchain.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hexagraph.cropchain.R

@Composable
fun RoleSelector(
    isUserFarmer: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RoleCard(
            modifier = Modifier.width(150.dp),
            title = stringResource(R.string.farmer),
            iconRes = R.drawable.farmer_icon_with_crop,
            isSelected = isUserFarmer,
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onChange(true)
            }
        )

        RoleCard(
            modifier = Modifier.width(150.dp),
            title = stringResource(R.string.scientist),
            iconRes = R.drawable.scientist,
            isSelected = !isUserFarmer,
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onChange(false)
            }
        )
    }
}


@Composable
fun RoleCard(
    modifier: Modifier,
    title: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .then(
                if (isSelected) Modifier.border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp)
                ) else Modifier
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
            .size(width = 150.dp, height = 180.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoleSelectorPreview() {
    var isFarmer by remember { mutableStateOf(true) }

    MaterialTheme {
        Surface {
            RoleSelector(
                isUserFarmer = isFarmer,
                onChange = { isFarmer = it },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
