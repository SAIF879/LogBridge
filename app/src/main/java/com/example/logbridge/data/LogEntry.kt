package com.example.logbridge.data

data class LogEntry(
    val timestamp: String?,
    val level: String?,
    val module: String?,
    val pid: String?,
    val thread: String?,
    val message: String,
    val error_code: String?
)
