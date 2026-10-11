package com.example.annakalender.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.annakalender.data.CalendarEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCalendarScreen(
    viewModel: CalendarViewModel,
    initialCreateEvent: Boolean = false
) {
    val events by viewModel.allEvents.collectAsState()
    val selectedDateMillis by viewModel.selectedDateMillis.collectAsState()
    val selectedView by viewModel.selectedView.collectAsState()

    var showDialog by remember { mutableStateOf(initialCreateEvent) }
    var selectedEvent by remember { mutableStateOf<CalendarEvent?>(null) }
    var initialDialogTimestamp by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(initialCreateEvent) {
        if (initialCreateEvent) {
            viewModel.goToToday()
            viewModel.selectView(CalendarViewType.DAY)
        }
    }

    val monthYearFormat = remember { SimpleDateFormat("MMMM yyyy", Locale.GERMANY) }
    val weekFormat = remember { SimpleDateFormat("dd.MM. - ", Locale.GERMANY) }
    val dayFormat = remember { SimpleDateFormat("dd. MMMM yyyy", Locale.GERMANY) }

    val topBarTitle = remember(selectedDateMillis, selectedView) {
        when (selectedView) {
            CalendarViewType.MONTH -> monthYearFormat.format(Date(selectedDateMillis))
            CalendarViewType.WEEK -> {
                val startOfWeek = CalendarUtils.getStartOfWeek(selectedDateMillis)
                val endOfWeek = CalendarUtils.addDays(startOfWeek, 6)
                val endFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
                "${weekFormat.format(Date(startOfWeek))}${endFormat.format(Date(endOfWeek))}"
            }
            CalendarViewType.DAY -> dayFormat.format(Date(selectedDateMillis))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    TextButton(
                        onClick = { viewModel.goToToday() }
                    ) {
                        Text(
                            text = "Heute",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = { viewModel.previousPeriod() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Vorherige",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = { viewModel.nextPeriod() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Nächste",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedView == CalendarViewType.MONTH,
                    onClick = { viewModel.selectView(CalendarViewType.MONTH) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Monat") },
                    label = { Text("Monat") }
                )
                NavigationBarItem(
                    selected = selectedView == CalendarViewType.WEEK,
                    onClick = { viewModel.selectView(CalendarViewType.WEEK) },
                    icon = { Icon(Icons.Default.ViewWeek, contentDescription = "Woche") },
                    label = { Text("Woche") }
                )
                NavigationBarItem(
                    selected = selectedView == CalendarViewType.DAY,
                    onClick = { viewModel.selectView(CalendarViewType.DAY) },
                    icon = { Icon(Icons.Default.Today, contentDescription = "Tag") },
                    label = { Text("Tag") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedEvent = null
                    initialDialogTimestamp = selectedDateMillis
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Neuer Termin"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedView) {
                CalendarViewType.MONTH -> {
                    MonthViewScreen(
                        selectedDateMillis = selectedDateMillis,
                        events = events,
                        onDaySelected = { viewModel.selectDate(it) },
                        onDayDoubleClicked = {
                            viewModel.selectDate(it)
                            viewModel.selectView(CalendarViewType.DAY)
                        },
                        onEventClicked = { event ->
                            selectedEvent = event
                            initialDialogTimestamp = null
                            showDialog = true
                        },
                        onAddEventForDate = { dateMillis ->
                            selectedEvent = null
                            initialDialogTimestamp = dateMillis
                            showDialog = true
                        }
                    )
                }
                CalendarViewType.WEEK -> {
                    WeekViewScreen(
                        selectedDateMillis = selectedDateMillis,
                        events = events,
                        onDaySelected = { viewModel.selectDate(it) },
                        onEventClicked = { event ->
                            selectedEvent = event
                            initialDialogTimestamp = null
                            showDialog = true
                        },
                        onAddEventForDateTime = { dateTimeMillis ->
                            selectedEvent = null
                            initialDialogTimestamp = dateTimeMillis
                            showDialog = true
                        }
                    )
                }
                CalendarViewType.DAY -> {
                    DayViewScreen(
                        selectedDateMillis = selectedDateMillis,
                        events = events,
                        onEventClicked = { event ->
                            selectedEvent = event
                            initialDialogTimestamp = null
                            showDialog = true
                        },
                        onAddEventForDateTime = { dateTimeMillis ->
                            selectedEvent = null
                            initialDialogTimestamp = dateTimeMillis
                            showDialog = true
                        }
                    )
                }
            }
        }

        if (showDialog) {
            AddEditEventDialog(
                event = selectedEvent,
                initialStartTimestamp = initialDialogTimestamp,
                onDismiss = { showDialog = false },
                onSave = { eventToSave ->
                    if (eventToSave.id == 0L) {
                        viewModel.insertEvent(eventToSave)
                    } else {
                        viewModel.updateEvent(eventToSave)
                    }
                    showDialog = false
                },
                onDelete = { eventToDelete ->
                    viewModel.deleteEvent(eventToDelete)
                    showDialog = false
                }
            )
        }
    }
}
