package com.example.annakalender.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annakalender.data.CalendarEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DayViewScreen(
    selectedDateMillis: Long,
    events: List<CalendarEvent>,
    onEventClicked: (CalendarEvent) -> Unit,
    onAddEventForDateTime: (Long) -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("EEEE, dd. MMMM yyyy", Locale.GERMANY) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.GERMANY) }

    val dayEvents = remember(selectedDateMillis, events) {
        events.filter { event ->
            val start = CalendarUtils.getStartOfDay(event.startTimestamp)
            val end = CalendarUtils.getStartOfDay(event.endTimestamp)
            selectedDateMillis in start..end
        }.sortedBy { it.startTimestamp }
    }

    val allDayEvents = remember(dayEvents) { dayEvents.filter { it.isAllDay } }
    val timedEvents = remember(dayEvents) { dayEvents.filter { !it.isAllDay } }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Date Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dateFormatter.format(Date(selectedDateMillis)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }

        // All-day events banner
        if (allDayEvents.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Ganztägig",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                allDayEvents.forEach { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clickable { onEventClicked(event) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
        }

        // 24 Hour Timeline
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Hour Labels (Left)
                Column(
                    modifier = Modifier
                        .width(60.dp)
                        .padding(top = 4.dp)
                ) {
                    for (hour in 0..23) {
                        Box(
                            modifier = Modifier
                                .height(64.dp)
                                .padding(end = 8.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Text(
                                text = String.format(Locale.GERMANY, "%02d:00", hour),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Hours Content Column
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    // Clickable Hour Rows
                    Column {
                        for (hour in 0..23) {
                            val hourSlotMillis = remember(selectedDateMillis, hour) {
                                Calendar.getInstance(Locale.GERMANY).apply {
                                    timeInMillis = selectedDateMillis
                                    set(Calendar.HOUR_OF_DAY, hour)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }.timeInMillis
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .border(
                                        0.5.dp,
                                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                    )
                                    .clickable { onAddEventForDateTime(hourSlotMillis) }
                            )
                        }
                    }

                    // Render Timed Events Overlay
                    timedEvents.forEach { event ->
                        val eventCal = Calendar.getInstance(Locale.GERMANY).apply { timeInMillis = event.startTimestamp }
                        val eventHour = eventCal.get(Calendar.HOUR_OF_DAY)
                        val eventMinute = eventCal.get(Calendar.MINUTE)

                        val topOffsetDp = (eventHour * 64 + (eventMinute / 60f * 64)).dp

                        val diff = (event.endTimestamp - event.startTimestamp) / 60000
                        val durationMinutes = diff.coerceAtLeast(30)
                        val heightDp = ((durationMinutes / 60f) * 64).dp.coerceAtLeast(36.dp)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 12.dp, top = topOffsetDp)
                                .height(heightDp)
                                .clickable { onEventClicked(event) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 4.dp, height = 40.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = event.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${timeFormat.format(Date(event.startTimestamp))} - ${timeFormat.format(Date(event.endTimestamp))}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (!event.location.isNullOrBlank()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = event.location,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
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
