package com.example.annakalender.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.annakalender.data.CalendarEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class CalendarViewModelTest {

    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setup() {
        viewModel = CalendarViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun defaultSelectedViewIsMonth() = runBlocking {
        assertEquals(CalendarViewType.MONTH, viewModel.selectedView.value)
    }

    @Test
    fun selectViewUpdatesState() = runBlocking {
        viewModel.selectView(CalendarViewType.WEEK)
        assertEquals(CalendarViewType.WEEK, viewModel.selectedView.value)

        viewModel.selectView(CalendarViewType.DAY)
        assertEquals(CalendarViewType.DAY, viewModel.selectedView.value)
    }

    @Test
    fun germanDateFormattingWorksCorrectly() {
        val cal = Calendar.getInstance(Locale.GERMANY).apply {
            set(2025, Calendar.OCTOBER, 3, 14, 30)
        }
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.GERMANY)

        assertEquals("03.10.2025", dateFormat.format(cal.time))
        assertEquals("14:30", timeFormat.format(cal.time))
    }

    @Test
    fun insertAndRetrieveEventInViewModel() = runBlocking {
        val event = CalendarEvent(
            id = 10,
            title = "Test ViewModel Event",
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 3600000
        )
        viewModel.insertEvent(event)

        val events = viewModel.allEvents.first()
        assertNotNull(events)
    }
}
