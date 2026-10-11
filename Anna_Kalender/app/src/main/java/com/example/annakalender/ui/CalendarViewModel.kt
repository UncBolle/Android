package com.example.annakalender.ui

import android.app.Application
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.annakalender.data.AppDatabase
import com.example.annakalender.data.CalendarEvent
import com.example.annakalender.data.CalendarRepository
import com.example.annakalender.widget.CalendarGlanceWidget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CalendarViewType {
    MONTH,
    WEEK,
    DAY
}

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CalendarRepository

    val allEvents: StateFlow<List<CalendarEvent>>

    private val _selectedDateMillis = MutableStateFlow(CalendarUtils.getStartOfDay(System.currentTimeMillis()))
    val selectedDateMillis: StateFlow<Long> = _selectedDateMillis.asStateFlow()

    private val _selectedView = MutableStateFlow(CalendarViewType.MONTH)
    val selectedView: StateFlow<CalendarViewType> = _selectedView.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).calendarEventDao()
        repository = CalendarRepository(dao)
        allEvents = repository.allEvents.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun selectDate(timeInMillis: Long) {
        _selectedDateMillis.value = CalendarUtils.getStartOfDay(timeInMillis)
    }

    fun selectView(viewType: CalendarViewType) {
        _selectedView.value = viewType
    }

    fun previousPeriod() {
        val current = _selectedDateMillis.value
        _selectedDateMillis.value = when (_selectedView.value) {
            CalendarViewType.MONTH -> CalendarUtils.addMonths(current, -1)
            CalendarViewType.WEEK -> CalendarUtils.addWeeks(current, -1)
            CalendarViewType.DAY -> CalendarUtils.addDays(current, -1)
        }
    }

    fun nextPeriod() {
        val current = _selectedDateMillis.value
        _selectedDateMillis.value = when (_selectedView.value) {
            CalendarViewType.MONTH -> CalendarUtils.addMonths(current, 1)
            CalendarViewType.WEEK -> CalendarUtils.addWeeks(current, 1)
            CalendarViewType.DAY -> CalendarUtils.addDays(current, 1)
        }
    }

    fun goToToday() {
        _selectedDateMillis.value = CalendarUtils.getStartOfDay(System.currentTimeMillis())
    }

    private fun updateWidgets() {
        viewModelScope.launch {
            try {
                CalendarGlanceWidget().updateAll(getApplication())
            } catch (_: Exception) {
                // Ignore if widget update fails in unit tests
            }
        }
    }

    fun insertEvent(event: CalendarEvent) {
        viewModelScope.launch {
            repository.insertEvent(event)
            updateWidgets()
        }
    }

    fun updateEvent(event: CalendarEvent) {
        viewModelScope.launch {
            repository.updateEvent(event)
            updateWidgets()
        }
    }

    fun deleteEvent(event: CalendarEvent) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            updateWidgets()
        }
    }
}
