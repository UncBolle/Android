package com.example.annakalender.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.annakalender.data.CalendarEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddEditEventDialog(
    event: CalendarEvent? = null,
    initialStartTimestamp: Long? = null,
    onDismiss: () -> Unit,
    onSave: (CalendarEvent) -> Unit,
    onDelete: ((CalendarEvent) -> Unit)? = null
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(event?.title ?: "") }
    var description by remember { mutableStateOf(event?.description ?: "") }
    var location by remember { mutableStateOf(event?.location ?: "") }
    var isAllDay by remember { mutableStateOf(event?.isAllDay ?: false) }
    var titleError by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val defaultStart = initialStartTimestamp ?: System.currentTimeMillis()
    var startTimestamp by remember { mutableStateOf(event?.startTimestamp ?: defaultStart) }
    var endTimestamp by remember { mutableStateOf(event?.endTimestamp ?: (defaultStart + 3600000)) }

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    val timeFormat = SimpleDateFormat("HH:mm", Locale.GERMANY)

    val startCal = remember(startTimestamp) {
        Calendar.getInstance().apply { timeInMillis = startTimestamp }
    }
    val endCal = remember(endTimestamp) {
        Calendar.getInstance().apply { timeInMillis = endTimestamp }
    }

    if (showDeleteConfirmDialog && event != null && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Termin löschen") },
            text = { Text("Möchtest du diesen Termin wirklich löschen?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDelete(event)
                    }
                ) {
                    Text("Löschen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (event == null) "Neuer Termin" else "Termin bearbeiten")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) titleError = false
                    },
                    label = { Text("Titel *") },
                    isError = titleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (titleError) {
                    Text(
                        text = "Bitte gib einen Titel ein",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Beschreibung") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Standort") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ganztägig",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isAllDay,
                        onCheckedChange = { isAllDay = it }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Start date & time pickers
                Text(
                    text = "Beginn",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val c = Calendar.getInstance().apply {
                                        timeInMillis = startTimestamp
                                        set(Calendar.YEAR, year)
                                        set(Calendar.MONTH, month)
                                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                    }
                                    startTimestamp = c.timeInMillis
                                    if (endTimestamp < startTimestamp) {
                                        endTimestamp = startTimestamp + 3600000
                                    }
                                },
                                startCal.get(Calendar.YEAR),
                                startCal.get(Calendar.MONTH),
                                startCal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    ) {
                        Text(dateFormat.format(startTimestamp))
                    }

                    if (!isAllDay) {
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        val c = Calendar.getInstance().apply {
                                            timeInMillis = startTimestamp
                                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                                            set(Calendar.MINUTE, minute)
                                        }
                                        startTimestamp = c.timeInMillis
                                        if (endTimestamp < startTimestamp) {
                                            endTimestamp = startTimestamp + 3600000
                                        }
                                    },
                                    startCal.get(Calendar.HOUR_OF_DAY),
                                    startCal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            }
                        ) {
                            Text(timeFormat.format(startTimestamp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // End date & time pickers
                Text(
                    text = "Ende",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val c = Calendar.getInstance().apply {
                                        timeInMillis = endTimestamp
                                        set(Calendar.YEAR, year)
                                        set(Calendar.MONTH, month)
                                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                    }
                                    endTimestamp = c.timeInMillis
                                },
                                endCal.get(Calendar.YEAR),
                                endCal.get(Calendar.MONTH),
                                endCal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    ) {
                        Text(dateFormat.format(endTimestamp))
                    }

                    if (!isAllDay) {
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        val c = Calendar.getInstance().apply {
                                            timeInMillis = endTimestamp
                                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                                            set(Calendar.MINUTE, minute)
                                        }
                                        endTimestamp = c.timeInMillis
                                    },
                                    endCal.get(Calendar.HOUR_OF_DAY),
                                    endCal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            }
                        ) {
                            Text(timeFormat.format(endTimestamp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        val newOrUpdatedEvent = CalendarEvent(
                            id = event?.id ?: 0,
                            title = title.trim(),
                            description = description.ifBlank { null },
                            location = location.ifBlank { null },
                            isAllDay = isAllDay,
                            startTimestamp = startTimestamp,
                            endTimestamp = endTimestamp,
                            colorHex = event?.colorHex ?: "#6200EE"
                        )
                        onSave(newOrUpdatedEvent)
                    }
                }
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            Row {
                if (event != null && onDelete != null) {
                    TextButton(
                        onClick = { showDeleteConfirmDialog = true }
                    ) {
                        Text(
                            text = "Löschen",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Abbrechen")
                }
            }
        }
    )
}
