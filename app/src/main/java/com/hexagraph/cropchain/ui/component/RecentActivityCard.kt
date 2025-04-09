package com.hexagraph.cropchain.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagraph.cropchain.domain.model.RecentActivityType

@Composable
fun RecentActivityCard(modifier: Modifier = Modifier,
                       imageUrl : String? = "QmX2nycET53VquRaCTCwsAVvrWpdRd7xLG6yVwewXB2qFQ",
                       timeStamp: String = "5th January 2025 12:00 PM",
                       type: RecentActivityType = RecentActivityType.UPLOADED){
//    Box(modifier = modifier
//        .fillMaxWidth()
//        .height(80.dp)){
//        Row(
//            modifier = Modifier.fillMaxWidth()
//                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
//        ) {
//            if (imageUrl != null) {
//                DisplayImageFromIPFS(
//                    cid = imageUrl,
//                    modifier = Modifier.width(100.dp)
//                        .clip(shape = RoundedCornerShape(8.dp))
//                )
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Column {
//                Text(
//                    text = type.title,
//                    fontSize = 18.sp,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = timeStamp,
//                    fontSize = 12.sp,
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                )
//            }
//
//        }
//        Image(
//            painter = painterResource(id = type.statusImage),
//            contentDescription = null,
//            modifier = Modifier.align(Alignment.CenterEnd)
//                .size(40.dp)
//                .padding(end = 16.dp)
//        )
//        HorizontalDivider(
//            modifier = Modifier.fillMaxWidth()
//                .align(Alignment.BottomCenter)
//        )
//    }
}

@Preview(showBackground = true)
@Composable
fun RecentActivityCardPreview(){
    RecentActivityCard()
}
