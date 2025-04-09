package com.hexagraph.cropchain.ui.component

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DisplayImageFromIPFS(
    modifier: Modifier = Modifier,
    cid: String) {

    Log.e("ipfs", cid)
    AsyncImage(
        model = cid,
        fallback = null,
        contentDescription = null,
        modifier = modifier.size(200.dp)
    )
}