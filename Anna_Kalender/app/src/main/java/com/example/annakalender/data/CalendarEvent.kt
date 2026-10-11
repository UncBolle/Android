package com.example.annakalender.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val isAllDay: Boolean = false,
    val colorHex: String = "#6200EE",
    val location: String? = null
)
