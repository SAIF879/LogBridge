package com.example.logbridge.utils.utiltyAndExtentions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Extension to format timestamp
fun String.formatTimestamp(): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm:ss")
        val parsed = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        parsed.format(formatter)
    } catch (e: Exception) {
        this
    }
}