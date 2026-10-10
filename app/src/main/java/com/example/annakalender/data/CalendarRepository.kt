package com.example.annakalender.data

import kotlinx.coroutines.flow.Flow

class CalendarRepository(private val dao: CalendarEventDao) {
    val allEvents: Flow<List<CalendarEvent>> = dao.getAllEvents()

    fun getEventsBetween(start: Long, end: Long): Flow<List<CalendarEvent>> {
        return dao.getEventsBetween(start, end)
    }

    suspend fun getEventById(id: Long): CalendarEvent? {
        return dao.getEventById(id)
    }

    suspend fun insertEvent(event: CalendarEvent): Long {
        return dao.insertEvent(event)
    }

    suspend fun updateEvent(event: CalendarEvent) {
        dao.updateEvent(event)
    }

    suspend fun deleteEvent(event: CalendarEvent) {
        dao.deleteEvent(event)
    }
}
