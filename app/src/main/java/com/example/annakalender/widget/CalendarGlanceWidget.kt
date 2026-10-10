package com.example.annakalender.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.annakalender.MainActivity
import com.example.annakalender.data.AppDatabase
import com.example.annakalender.data.CalendarEvent
import com.example.annakalender.ui.CalendarUtils
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val now = System.currentTimeMillis()
        val startOfToday = CalendarUtils.getStartOfDay(now)
        val endOfToday = CalendarUtils.getEndOfDay(now)

        val dao = AppDatabase.getDatabase(context).calendarEventDao()
        val todayEvents = try {
            dao.getEventsBetween(startOfToday, endOfToday).first()
        } catch (_: Exception) {
            emptyList()
        }

        provideContent {
            GlanceTheme {
                WidgetContent(
                    context = context,
                    todayEvents = todayEvents
                )
            }
        }
    }

    @Composable
    private fun WidgetContent(
        context: Context,
        todayEvents: List<CalendarEvent>
    ) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.GERMANY)
        val todayString = "Heute, " + SimpleDateFormat("dd. MMMM", Locale.GERMANY).format(Date())

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val createEventIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_CREATE_EVENT", true)
        }

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(12.dp)
                .background(GlanceTheme.colors.background)
                .clickable(actionStartActivity(openAppIntent))
        ) {
            // Header Row
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = GlanceModifier.defaultWeight()
                ) {
                    Text(
                        text = todayString,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.onBackground
                        )
                    )
                }

                Button(
                    text = "+",
                    onClick = actionStartActivity(createEventIntent)
                )
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // Events List or Empty State
            if (todayEvents.isEmpty()) {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Keine Termine heute",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = GlanceTheme.colors.onSurfaceVariant
                        )
                    )
                }
            } else {
                Column(
                    modifier = GlanceModifier.fillMaxWidth()
                ) {
                    todayEvents.take(4).forEach { event ->
                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val timeText = if (event.isAllDay) {
                                "Ganztägig"
                            } else {
                                timeFormat.format(Date(event.startTimestamp))
                            }

                            Text(
                                text = timeText,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GlanceTheme.colors.primary
                                )
                            )

                            Spacer(modifier = GlanceModifier.width(8.dp))

                            Text(
                                text = event.title,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    color = GlanceTheme.colors.onBackground
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
