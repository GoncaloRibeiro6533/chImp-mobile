package pt.isel.chimp.profile.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.profile.CANCEL_BUTTON_TAG
import pt.isel.chimp.profile.EditingUsernameView
import pt.isel.chimp.profile.ProfileScreenState
import pt.isel.chimp.profile.SAVE_BUTTON_TAG
import pt.isel.chimp.profile.USERNAME_TEXT_TAG

@RunWith(AndroidJUnit4::class)
class ProfileEditingViewTests {

    @get:Rule
    val composeTree = createComposeRule()

    @Test
    fun username_is_displayed() {
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            EditingUsernameView(
                state = ProfileScreenState.EditingUsername(expected),
                onSaveIntent = { /* no-op */ },
                onCancelIntent = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(USERNAME_TEXT_TAG)
            .assertIsDisplayed()
            .assertTextContains(expected.username)
    }

    @Test
    fun buttons_are_in_correct_state() {
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            EditingUsernameView(
                state = ProfileScreenState.EditingUsername(expected),
                onSaveIntent = { /* no-op */ },
                onCancelIntent = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(SAVE_BUTTON_TAG)
            .assertIsDisplayed()
            .assertIsNotEnabled()
        composeTree
            .onNodeWithTag(CANCEL_BUTTON_TAG)
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun buttons_are_in_correct_state_after_editing() {
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            EditingUsernameView(
                state = ProfileScreenState.EditingUsername(expected),
                onSaveIntent = { /* no-op */ },
                onCancelIntent = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(USERNAME_TEXT_TAG).performTextInput("Cat")
        composeTree.onNodeWithTag(SAVE_BUTTON_TAG).assertIsEnabled()
        composeTree.onNodeWithTag(CANCEL_BUTTON_TAG).assertIsEnabled()
    }

    @Test
    fun buttons_are_in_correct_state_after_editing_and_deleting_the_modifications() {
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            EditingUsernameView(
                state = ProfileScreenState.EditingUsername(expected),
                onSaveIntent = { /* no-op */ },
                onCancelIntent = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(USERNAME_TEXT_TAG).performTextInput("Cat")
        composeTree
            .onNodeWithTag(USERNAME_TEXT_TAG).performTextReplacement(expected.username)
        composeTree.onNodeWithTag(SAVE_BUTTON_TAG).assertIsNotEnabled()
        composeTree.onNodeWithTag(CANCEL_BUTTON_TAG).assertIsEnabled()
    }

    @Test
    fun onCancelIntent_is_called_when_cancel_button_is_pressed() {
        var cancelIntentCalled = ""
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            EditingUsernameView(
                state = ProfileScreenState.EditingUsername(expected),
                onSaveIntent = { /* no-op */ },
                onCancelIntent = { cancelIntentCalled = expected.username },
            )
        }
        composeTree
            .onNodeWithTag(CANCEL_BUTTON_TAG)
            .performClick()
        assertEquals(expected.username, cancelIntentCalled)
    }

    @Test
    fun onSaveIntent_is_called_when_save_button_is_pressed() {
        var saveIntentCalled = ""
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            EditingUsernameView(
                state = ProfileScreenState.EditingUsername(expected),
                onSaveIntent = { saveIntentCalled = "BobCat" },
                onCancelIntent = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(USERNAME_TEXT_TAG).performTextReplacement("BobCat")
        composeTree.onNodeWithTag(SAVE_BUTTON_TAG).performClick()
        assertEquals("BobCat", saveIntentCalled)
    }
}