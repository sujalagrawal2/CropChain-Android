package com.hexagraph.cropchain.farmer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.domain.model.SupportedLanguages
import com.hexagraph.cropchain.ui.component.AppButton
import com.hexagraph.cropchain.ui.component.LanguageSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePreferenceBottomSheet(
    onDismissRequest: () -> Unit,
    selectedLanguage: SupportedLanguages,
    onChangeSelectedLanguage: (SupportedLanguages) -> Unit,
    onSaveSelectedLanguage: ()->Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LanguageSelector(
                onChangeSelectedLanguage = onChangeSelectedLanguage,
                selectedLanguage = selectedLanguage,
                modifier = Modifier
            )
            Spacer(Modifier.height(40.dp))
            AppButton(
                modifier = Modifier.fillMaxWidth(0.75f),
                isEnabled = true,
                text = stringResource(R.string.save),
                onClick = {
                    onDismissRequest()
                    onSaveSelectedLanguage()
                }
            )
        }
    }
}