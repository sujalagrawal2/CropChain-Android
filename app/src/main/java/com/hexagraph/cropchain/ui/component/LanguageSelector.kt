package com.hexagraph.cropchain.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagraph.cropchain.domain.model.SupportedLanguages

@Composable
fun LanguageSelector(
    modifier: Modifier,
    selectedLanguage: SupportedLanguages,
    onChangeSelectedLanguage: (SupportedLanguages) -> Unit
) {
    val languages = SupportedLanguages.entries
    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current

    // Scroll to selected item when it changes
    LaunchedEffect(selectedLanguage) {
        val index = languages.indexOf(selectedLanguage)
        val centeredOffset = maxOf(index - 2, 0)
        listState.animateScrollToItem(centeredOffset)
    }
    Box(modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(languages.size) { index ->
                val language = languages[index]
                val isSelected = language == selectedLanguage

                val animatedFontSize by animateDpAsState(
                    targetValue = if (isSelected) 18.sp.value.dp else 14.sp.value.dp,
                    label = "fontSize"
                )
                val animatedFontWeight by animateIntAsState(
                    targetValue = if (isSelected) FontWeight.Bold.weight else FontWeight.Normal.weight,
                    label = "fontWeight"
                )

                val textColor = MaterialTheme.colorScheme.onBackground
                val selectedBackground = MaterialTheme.colorScheme.surfaceVariant

                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) selectedBackground else Color.Transparent)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onChangeSelectedLanguage(language)
                        }
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "${language.languageNameInNormalScript} (${language.languageNameInNativeScript})",
                        color = textColor,
                        fontSize = animatedFontSize.value.sp,
                        fontWeight = FontWeight(animatedFontWeight),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LanguageSelectorPreview() {
    MaterialTheme {
        Surface() {
            var selectedLanguage by remember { mutableStateOf(SupportedLanguages.ENGLISH) }
            LanguageSelector(
                modifier = Modifier,
                selectedLanguage = selectedLanguage,
                onChangeSelectedLanguage = { selectedLanguage = it }
            )
        }
    }
}
