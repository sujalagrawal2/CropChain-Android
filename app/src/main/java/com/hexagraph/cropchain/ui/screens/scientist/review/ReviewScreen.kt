package com.hexagraph.cropchain.ui.screens.scientist.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.ui.component.DisplayImageList
import com.hexagraph.cropchain.ui.screens.farmer.uploadedImages.CropItem

@Composable
fun ReviewScreen(
    viewModel: ReviewScreenViewModel = hiltViewModel(),
    onBackButtonPressed: () -> Unit = {},
    onImageSelected: (String, Int) -> Unit = { _, _ -> }
) {
    val uiState = viewModel.uiState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "Review Screen",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        var selectedTab by remember { mutableStateOf("Verify") }
        val imageSections =
            if (selectedTab == "Verify") uiState.value.verifyImages
            else uiState.value.reviewImages


        val imagesUrl: MutableList<CropItem> = emptyList<CropItem>().toMutableList()
        for (images in imageSections) {
            val splitImages = images.url.split("$").filter { it.isNotBlank() }
            imagesUrl.add(images.copy(url = splitImages[0]))
        }
        UpperScreen(onTabSelected = { selectedTab = it }, selectedTab = selectedTab)

        DisplayImageList(
            imagesUrl,
            onClickImage = {
                onImageSelected(
                    imageSections[it].id.toString(),
                    if (selectedTab == "Verify") 2 else 1
                )
            })

    }
}


@Composable
fun UpperScreen(onTabSelected: (String) -> Unit, selectedTab: String) {


    val tabs = listOf("Verify", "Review")


    val tabBackground = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
    val selectedTabColor = MaterialTheme.colorScheme.primary
    val unselectedTabColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = tabBackground),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) selectedTabColor.copy(alpha = 0.2f) else Color.Transparent
                        )
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) selectedTabColor else unselectedTabColor,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

            }
        }
//        Spacer(modifier = Modifier.height(8.dp))
    }
}