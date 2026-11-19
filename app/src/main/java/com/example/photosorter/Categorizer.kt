package com.example.photosorter

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** Groups photos into categories by the month they were taken, newest first. */
object Categorizer {

    private const val NO_DATE = "Unknown date"

    fun byMonth(photos: List<Photo>): List<Category> {
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return photos
            .groupBy { photo ->
                photo.dateTakenMillis?.let { formatter.format(Date(it)) } ?: NO_DATE
            }
            .map { (title, items) -> Category(title, items) }
            .sortedWith(
                compareByDescending<Category> { it.photos.firstOrNull()?.dateTakenMillis ?: Long.MIN_VALUE }
            )
    }
}
