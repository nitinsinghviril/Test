package com.example.photosorter

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore

/** Reads every image on the device from MediaStore. */
object PhotoRepository {

    fun loadPhotos(context: Context): List<Photo> {
        val resolver: ContentResolver = context.contentResolver
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED,
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val photos = mutableListOf<Photo>()
        resolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val takenCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val addedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val uri = android.content.ContentUris.withAppendedId(collection, id)
                val taken = cursor.getLong(takenCol).takeIf { it > 0 }
                // DATE_ADDED is in seconds; fall back to it when DATE_TAKEN is missing.
                val fallback = cursor.getLong(addedCol).takeIf { it > 0 }?.times(1000)
                photos += Photo(uri, taken ?: fallback)
            }
        }
        return photos
    }
}
