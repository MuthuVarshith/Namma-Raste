package com.muthuvarshith.nammaraste.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey val ticketId: String,
    val issueType: String,
    val severity: String,
    val description: String,
    val status: String = "Submitted",
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val imagePath: String,
    val reporterUsername: String
)
