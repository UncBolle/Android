package com.example.annakalender.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarEventDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: CalendarEventDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.calendarEventDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetEventById() = runBlocking {
        val event = CalendarEvent(
            id = 1,
            title = "Zahnarzt",
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 3600000
        )
        dao.insertEvent(event)

        val retrieved = dao.getEventById(1)
        assertEquals("Zahnarzt", retrieved?.title)
    }

    @Test
    fun insertEventWithNullableFields() = runBlocking {
        val event = CalendarEvent(
            id = 2,
            title = "Test ohne Details",
            description = null,
            location = null,
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 3600000
        )
        dao.insertEvent(event)

        val retrieved = dao.getEventById(2)
        assertEquals("Test ohne Details", retrieved?.title)
        assertNull(retrieved?.description)
        assertNull(retrieved?.location)
    }

    @Test
    fun getEventsBetweenFiltersByDateRange() = runBlocking {
        val now = System.currentTimeMillis()
        val eventYesterday = CalendarEvent(
            id = 1,
            title = "Gestern",
            startTimestamp = now - 86400000,
            endTimestamp = now - 82800000
        )
        val eventToday = CalendarEvent(
            id = 2,
            title = "Heute",
            startTimestamp = now,
            endTimestamp = now + 3600000
        )
        val eventNextWeek = CalendarEvent(
            id = 3,
            title = "Nächste Woche",
            startTimestamp = now + 7 * 86400000,
            endTimestamp = now + 7 * 86400000 + 3600000
        )

        dao.insertEvent(eventYesterday)
        dao.insertEvent(eventToday)
        dao.insertEvent(eventNextWeek)

        val todayEvents = dao.getEventsBetween(now - 1000, now + 3600000 + 1000).first()
        assertEquals(1, todayEvents.size)
        assertEquals("Heute", todayEvents[0].title)
    }

    @Test
    fun updateEventModifiesData() = runBlocking {
        val event = CalendarEvent(
            id = 1,
            title = "Meeting",
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 3600000
        )
        dao.insertEvent(event)

        val updatedEvent = event.copy(title = "Wichtiges Meeting")
        dao.updateEvent(updatedEvent)

        val retrieved = dao.getEventById(1)
        assertEquals("Wichtiges Meeting", retrieved?.title)
    }

    @Test
    fun deleteEventRemovesFromDb() = runBlocking {
        val event = CalendarEvent(
            id = 1,
            title = "Meeting",
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 3600000
        )
        dao.insertEvent(event)
        dao.deleteEvent(event)

        val retrieved = dao.getEventById(1)
        assertNull(retrieved)
    }
}
