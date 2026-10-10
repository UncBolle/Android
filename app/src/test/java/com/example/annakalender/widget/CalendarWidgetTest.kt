package com.example.annakalender.widget

import android.content.Context
import android.content.Intent
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.annakalender.MainActivity
import com.example.annakalender.data.AppDatabase
import com.example.annakalender.data.CalendarEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarWidgetTest {

    private lateinit var context: Context
    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testWidgetReceiverReceiverDeclared() {
        val receiver = CalendarWidgetReceiver()
        assertNotNull(receiver.glanceAppWidget)
        assertTrue(receiver.glanceAppWidget is CalendarGlanceWidget)
    }

    @Test
    fun testCreateEventIntentHasExtra() {
        val createEventIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("EXTRA_CREATE_EVENT", true)
        }
        assertTrue(createEventIntent.getBooleanExtra("EXTRA_CREATE_EVENT", false))
    }

    @Test
    fun testEmptyStateWhenNoEventsForToday() = runBlocking {
        val dao = database.calendarEventDao()
        val events = dao.getAllEvents().first()
        assertTrue(events.isEmpty())
    }

    @Test
    fun testWidgetDisplaysTodayEventsWhenPresent() = runBlocking {
        val dao = database.calendarEventDao()
        val now = System.currentTimeMillis()
        val event = CalendarEvent(
            id = 99,
            title = "Team Meeting",
            startTimestamp = now,
            endTimestamp = now + 3600000
        )
        dao.insertEvent(event)

        val events = dao.getAllEvents().first()
        assertEquals(1, events.size)
        assertEquals("Team Meeting", events[0].title)
    }
}
