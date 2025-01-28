package com.hexagraph.cropchain.domain.model

import com.hexagraph.cropchain.R

enum class RecentActivityType(
    val title: String,
    val statusImage: Int
) {
  VERIFIED_BY_SCIENTIST(
        title = "Disease Diagnosed!!",
        statusImage = R.drawable.verified_icon_new,
    ),
    VERIFICATION_PENDING(
        title = "Verification Pending",
        statusImage = R.drawable.review_pending,
    ),
    UPLOADED(
        title = "Image Uploaded!!",
        statusImage = R.drawable.uploadsuccessicon,
    ),
    IMAGE_NOT_UPLOADED(
        title = "Image Upload Failed",
        statusImage = R.drawable.uplaod_failed,
    ),
}