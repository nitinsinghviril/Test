package com.example.photosorter

import android.net.Uri

data class Photo(
    val uri: Uri,
    val dateTakenMillis: Long?,
)

/** A named group of photos, e.g. "June 2024". */
data class Category(
    val title: String,
    val photos: List<Photo>,
)
