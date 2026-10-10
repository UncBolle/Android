package com.example.annakalender.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarEventDao {
    @Query("SELECT * FROM events ORDER BY startTimestamp ASC")
    fun getAllEvents(): Flow<List<CalendarEvent>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): CalendarEvent?

    @Query("SELECT * FROM events WHERE startTimestamp <= :endTimestamp AND endTimestamp >= :startTimestamp ORDER BY startTimestamp ASC")
    fun getEventsBetween(startTimestamp: Long, endTimestamp: Long): Flow<List<CalendarEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEvent): Long

    @Update
    suspend fun updateEvent(event: CalendarEvent)

    @Delete
    suspend fun deleteEvent(event: CalendarEvent)
}
