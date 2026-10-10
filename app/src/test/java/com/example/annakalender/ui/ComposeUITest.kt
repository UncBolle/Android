package com.example.annakalender.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.annakalender.data.CalendarEvent
import com.example.annakalender.ui.theme.AnnaKalenderTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setup() {
        viewModel = CalendarViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testUI01NavigationBetweenViews() {
        composeTestRule.setContent {
            AnnaKalenderTheme {
                MainCalendarScreen(viewModel = viewModel)
            }
        }

        // Verify Navigation Bar tabs exist in main screen
        composeTestRule.onNodeWithText("Monat").assertIsDisplayed()
        composeTestRule.onNodeWithText("Woche").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tag").assertIsDisplayed()

        // Click on "Woche"
        composeTestRule.onNodeWithText("Woche").performClick()

        // Click on "Tag"
        composeTestRule.onNodeWithText("Tag").performClick()
    }

    @Test
    fun testUI02CreateEventViaForm() {
        var savedEvent: CalendarEvent? = null

        composeTestRule.setContent {
            AnnaKalenderTheme {
                AddEditEventDialog(
                    event = null,
                    onDismiss = {},
                    onSave = { savedEvent = it }
                )
            }
        }

        // Verify Dialog Title
        composeTestRule.onNodeWithText("Neuer Termin").assertIsDisplayed()

        // Type Title
        composeTestRule.onNodeWithText("Titel *").performTextInput("Projektbesprechung")

        // Type Location
        composeTestRule.onNodeWithText("Standort").performTextInput("Büro Raum 3")

        // Click Save
        composeTestRule.onNodeWithText("Speichern").performClick()

        // Verify event was saved with correct title and location
        assertTrue(savedEvent != null)
        assertEquals("Projektbesprechung", savedEvent?.title)
        assertEquals("Büro Raum 3", savedEvent?.location)
    }

    @Test
    fun testUI03DeleteEventWithConfirmationDialog() {
        var deletedEvent: CalendarEvent? = null
        val sampleEvent = CalendarEvent(
            id = 1,
            title = "Test-Termin",
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + 3600000
        )

        composeTestRule.setContent {
            AnnaKalenderTheme {
                AddEditEventDialog(
                    event = sampleEvent,
                    onDismiss = {},
                    onSave = {},
                    onDelete = { deletedEvent = it }
                )
            }
        }

        // Click "Löschen" button on edit dialog
        composeTestRule.onNodeWithText("Löschen").performClick()

        // Verify confirmation dialog message
        composeTestRule.onNodeWithText("Möchtest du diesen Termin wirklich löschen?").assertIsDisplayed()

        // Confirm deletion by clicking "Löschen" button on confirm dialog
        composeTestRule.onAllNodesWithText("Löschen")[1].performClick()

        // Verify onDelete callback was invoked with sampleEvent
        assertTrue(deletedEvent != null)
        assertEquals("Test-Termin", deletedEvent?.title)
    }

    @Test
    fun testUI04GermanLabelsConsistency() {
        composeTestRule.setContent {
            AnnaKalenderTheme {
                MainCalendarScreen(viewModel = viewModel)
            }
        }

        // Check German UI terms on main screen
        composeTestRule.onNodeWithText("Heute").assertIsDisplayed()
        composeTestRule.onNodeWithText("Monat").assertIsDisplayed()
        composeTestRule.onNodeWithText("Woche").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tag").assertIsDisplayed()
    }
}
