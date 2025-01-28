package com.hexagraph.cropchain.ui.component

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DisplayImageFromIPFS(cid: String) {
    val imageUrl = "https://gateway.pinata.cloud/ipfs/$cid"

    Log.e("ipfs",imageUrl)
    AsyncImage(
        model = imageUrl,
        fallback = null,
        contentDescription = null,
        modifier = Modifier.size(200.dp)
    )
}