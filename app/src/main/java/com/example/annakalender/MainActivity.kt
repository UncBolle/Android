package com.example.annakalender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.annakalender.ui.CalendarViewModel
import com.example.annakalender.ui.MainCalendarScreen
import com.example.annakalender.ui.theme.AnnaKalenderTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val shouldCreateEvent = intent?.getBooleanExtra("EXTRA_CREATE_EVENT", false) ?: false

        setContent {
            AnnaKalenderTheme {
                MainCalendarScreen(
                    viewModel = viewModel,
                    initialCreateEvent = shouldCreateEvent
                )
            }
        }
    }
}
