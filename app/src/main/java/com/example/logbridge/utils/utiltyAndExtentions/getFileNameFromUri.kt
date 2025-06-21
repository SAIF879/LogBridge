package com.example.logbridge.utils.utiltyAndExtentions

import android.provider.OpenableColumns
import android.content.Context
import android.net.Uri

// Utility to extract file name from URI
 fun getFileNameFromUri(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri,
        null, null,
        null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                return it.getString(nameIndex)
            }
        }
    }
    return null
}
