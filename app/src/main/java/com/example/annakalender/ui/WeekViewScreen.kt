package com.example.annakalender.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annakalender.data.CalendarEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun WeekViewScreen(
    selectedDateMillis: Long,
    events: List<CalendarEvent>,
    onDaySelected: (Long) -> Unit,
    onEventClicked: (CalendarEvent) -> Unit,
    onAddEventForDateTime: (Long) -> Unit
) {
    val startOfWeekMillis = remember(selectedDateMillis) {
        CalendarUtils.getStartOfWeek(selectedDateMillis)
    }
    val weekDays = remember(startOfWeekMillis) {
        CalendarUtils.getWeekDays(startOfWeekMillis)
    }
    val todayMillis = remember { CalendarUtils.getStartOfDay(System.currentTimeMillis()) }

    val dayFormat = remember { SimpleDateFormat("E", Locale.GERMANY) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.GERMANY) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Week Days Header (7 columns)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp) // Offset for time column
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weekDays.forEach { dayMillis ->
                val isToday = CalendarUtils.isSameDay(dayMillis, todayMillis)
                val isSelected = CalendarUtils.isSameDay(dayMillis, selectedDateMillis)

                val dayCal = remember(dayMillis) {
                    Calendar.getInstance(Locale.GERMANY).apply { timeInMillis = dayMillis }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surface
                            }
                        )
                        .then(
                            if (isToday) Modifier.border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            ) else Modifier
                        )
                        .clickable { onDaySelected(dayMillis) }
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = dayFormat.format(Date(dayMillis)).take(2),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dayCal.get(Calendar.DAY_OF_MONTH).toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        HorizontalDivider()

        // 24 Hour Time Grid
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Time column on the left
                Column(
                    modifier = Modifier
                        .width(50.dp)
                        .padding(top = 4.dp)
                ) {
                    for (hour in 0..23) {
                        Box(
                            modifier = Modifier
                                .height(56.dp)
                                .padding(end = 4.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Text(
                                text = String.format(Locale.GERMANY, "%02d:00", hour),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // 7 Day Grid Columns
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    weekDays.forEach { dayMillis ->
                        val dayEvents = remember(dayMillis, events) {
                            events.filter { event ->
                                val start = CalendarUtils.getStartOfDay(event.startTimestamp)
                                val end = CalendarUtils.getStartOfDay(event.endTimestamp)
                                dayMillis in start..end
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            // Hour slots
                            Column {
                                for (hour in 0..23) {
                                    val hourSlotMillis = remember(dayMillis, hour) {
                                        Calendar.getInstance(Locale.GERMANY).apply {
                                            timeInMillis = dayMillis
                                            set(Calendar.HOUR_OF_DAY, hour)
                                            set(Calendar.MINUTE, 0)
                                            set(Calendar.SECOND, 0)
                                            set(Calendar.MILLISECOND, 0)
                                        }.timeInMillis
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .border(0.2.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                            .clickable { onAddEventForDateTime(hourSlotMillis) }
                                    )
                                }
                            }

                            // Render events overlay for this day
                            dayEvents.forEach { event ->
                                val eventCal = Calendar.getInstance(Locale.GERMANY).apply { timeInMillis = event.startTimestamp }
                                val eventHour = if (event.isAllDay) 0 else eventCal.get(Calendar.HOUR_OF_DAY)
                                val eventMinute = if (event.isAllDay) 0 else eventCal.get(Calendar.MINUTE)

                                val topOffsetDp = (eventHour * 56 + (eventMinute / 60f * 56)).dp

                                val durationMinutes = if (event.isAllDay) 60 else {
                                    val diff = (event.endTimestamp - event.startTimestamp) / 60000
                                    diff.coerceAtLeast(30)
                                }
                                val heightDp = ((durationMinutes / 60f) * 56).dp.coerceAtLeast(28.dp)

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 1.dp, end = 1.dp, top = topOffsetDp)
                                        .height(heightDp)
                                        .clickable { onEventClicked(event) },
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(2.dp)
                                    ) {
                                        Text(
                                            text = event.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        if (!event.isAllDay) {
                                            Text(
                                                text = timeFormat.format(Date(event.startTimestamp)),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 8.sp,
                                                maxLines = 1,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
