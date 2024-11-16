package pt.isel.chimp.profile.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.profile.EDIT_USERNAME_BUTTON_TAG
import pt.isel.chimp.profile.EMAIL_TEXT_TAG
import pt.isel.chimp.profile.ProfileScreenState
import pt.isel.chimp.profile.ProfileView
import pt.isel.chimp.profile.USERNAME_TEXT_TAG

@RunWith(AndroidJUnit4::class)
class ProfileViewTests {

    @get:Rule
    val composeTree = createComposeRule()

    @Test
    fun username_is_displayed() {
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            ProfileView(
                state = ProfileScreenState.Success(expected),
                onEditUsernameClick = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(USERNAME_TEXT_TAG)
            .assertIsDisplayed()
            .assertTextContains(expected.username)
        composeTree
            .onNodeWithTag(EMAIL_TEXT_TAG)
            .assertIsDisplayed()
            .assertTextContains(expected.email)

    }

    @Test
    fun edit_icon_is_enabled() {
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            ProfileView(
                state = ProfileScreenState.Success(expected),
                onEditUsernameClick = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(EDIT_USERNAME_BUTTON_TAG)
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun onEditIntent_is_called_when_icon_is_pressed() {
        var editIntentCalled = false
        val expected = Profile("Bob", "bob@example.com")
        composeTree.setContent {
            ProfileView(
                state = ProfileScreenState.Success(expected),
                onEditUsernameClick = { editIntentCalled = true },
            )
        }
        composeTree
            .onNodeWithTag(EDIT_USERNAME_BUTTON_TAG)
            .performClick()
        assert(editIntentCalled)
    }
}
