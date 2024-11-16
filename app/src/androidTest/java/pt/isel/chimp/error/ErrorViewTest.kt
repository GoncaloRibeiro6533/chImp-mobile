package pt.isel.chimp.error

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.profile.ERROR_ALERT
import pt.isel.chimp.profile.ERROR_ALERT_MESSAGE
import pt.isel.chimp.profile.ERROR_DISMISS_BUTTON
import pt.isel.chimp.profile.ERROR_ALERT_TITLE
import pt.isel.chimp.profile.ErrorAlert

@RunWith(AndroidJUnit4::class)
class ErrorViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testErrorAlert_displays_all_items() {
        composeTestRule.setContent {
            ErrorAlert(
                title = "Error",
                message = "An error occurred",
                buttonText = "Dismiss",
                onDismiss = {}
            )
        }
        composeTestRule.onNodeWithTag(ERROR_ALERT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_ALERT_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_ALERT_MESSAGE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_DISMISS_BUTTON).assertIsDisplayed()
    }

    @Test
    fun testErrorAlert_displays_correct_icon() {
        composeTestRule.setContent {
            ErrorAlert(
                title = "Error",
                message = "An error occurred",
                buttonText = "Dismiss",
                onDismiss = {}
            )
        }
        composeTestRule.onNodeWithTag(ERROR_ALERT).assert(hasTestTag(ERROR_ALERT))
    }

    @Test
    fun testErrorAlert_displays_correct_text() {
        val title = "Error"
        val message = "An error occurred"
        val buttonText = "Dismiss"
        composeTestRule.setContent {
            ErrorAlert(
                title = title,
                message = message,
                buttonText = buttonText,
                onDismiss = {}
            )
        }
        composeTestRule.onNodeWithTag(ERROR_ALERT_TITLE).assert(hasText(title))
        composeTestRule.onNodeWithTag(ERROR_ALERT_MESSAGE).assert(hasText(message))
        composeTestRule.onNodeWithTag(ERROR_DISMISS_BUTTON).assert(hasText(buttonText))
    }

    @Test
    fun when_dismiss_button_clicked_then_dismiss_action_is_called() {
        var dismissed = false
        composeTestRule.setContent {
            ErrorAlert(
                title = "Error",
                message = "An error occurred",
                buttonText = "Dismiss",
                onDismiss = { dismissed = true }
            )
        }
        composeTestRule.onNodeWithTag(ERROR_DISMISS_BUTTON).performClick()
        assert(dismissed)
    }
}
