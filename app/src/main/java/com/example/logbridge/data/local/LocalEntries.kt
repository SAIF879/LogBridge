package com.example.logbridge.data.local

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LocalEntries(
    @Id(assignable = true) var id: Long = 0,
    var fileName: String,
    var filePath: String,
)