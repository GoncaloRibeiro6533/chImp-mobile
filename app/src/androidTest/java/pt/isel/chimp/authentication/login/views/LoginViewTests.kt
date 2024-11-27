package pt.isel.chimp.authentication.login.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.authentication.login.LOGIN_BUTTON
import pt.isel.chimp.authentication.login.LOGIN_TEXT_FIELDS
import pt.isel.chimp.authentication.login.LOGIN_VIEW
import pt.isel.chimp.authentication.login.LoginView
import pt.isel.chimp.authentication.login.REGISTER_ANCHOR
import pt.isel.chimp.authentication.login.components.LOGIN_PASSWORD_TEXT_FIELD
import pt.isel.chimp.authentication.login.components.LOGIN_USERNAME_TEXT_FIELD

@RunWith(AndroidJUnit4::class)
class LoginViewTests {

    @get:Rule
    val composeTree = createComposeRule()

    @Test
    fun testLoginView_displays_all_items() {
        composeTree.setContent {
            LoginView(
                onSubmit = { _, _ -> },
                onRegisterRequested = {}
            )
        }
        composeTree.onNodeWithTag(LOGIN_VIEW).assertIsDisplayed()
        composeTree.onNodeWithTag(LOGIN_TEXT_FIELDS).assertIsDisplayed()
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(REGISTER_ANCHOR).assertIsDisplayed()
    }

    @Test
    fun testLoginView_button_is_disabled_until_all_textFields_are_written_and_valid() {
        composeTree.setContent {
            LoginView(
                onSubmit = { _, _ -> },
                onRegisterRequested = {}
            )
        }
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).performTextInput("Bob")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).performTextInput("password_of_bob")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsEnabled()
    }

    @Test
    fun testLoginView_button_is_disabled_when_username_textField_does_not_have_a_minimum_length() {
        composeTree.setContent {
            LoginView(
                onSubmit = { _, _ -> },
                onRegisterRequested = {}
            )
        }
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).performTextInput("B")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).performTextInput("password_of_bob")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).performTextReplacement("Bob")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsEnabled()
    }

    @Test
    fun testLoginView_button_is_disabled_when_password_textField_does_not_have_a_minimum_length() {
        composeTree.setContent {
            LoginView(
                onSubmit = { _, _ -> },
                onRegisterRequested = {}
            )
        }
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).performTextInput("Bob")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).performTextInput("p")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).performTextReplacement("password_of_bob")
        composeTree.onNodeWithTag(LOGIN_BUTTON).assertIsEnabled()
    }

    @Test
    fun when_login_button_clicked_then_onSubmit_action_is_called() {
        var submitted = false
        composeTree.setContent {
            LoginView(
                onSubmit = { _, _ -> submitted = true },
                onRegisterRequested = {}
            )
        }
        composeTree.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).performTextInput("Bob")
        composeTree.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).performTextInput("password_of_bob")
        composeTree.onNodeWithTag(LOGIN_BUTTON).performClick()
        assert(submitted)
    }

    @Test
    fun when_register_anchor_clicked_then_onRegisterRequested_action_is_called() {
        var registered = false
        composeTree.setContent {
            LoginView(
                onSubmit = { _, _ -> },
                onRegisterRequested = { registered = true }
            )
        }
        composeTree.onNodeWithTag(REGISTER_ANCHOR).performClick()
        assert(registered)
    }

    @Test
    fun does_not_save_or_accept_spaces(){
        var username = ""
        var password = ""
        composeTree.setContent {
            LoginView(
                onSubmit = { u, p ->
                    username = u
                    password = p
                },
                onRegisterRequested = {}
            )
        }
        composeTree.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).performTextInput("  Bob  ")
        composeTree.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).performTextInput("  password_of_bob  ")
        composeTree.onNodeWithTag(LOGIN_BUTTON).performClick()
        assert( username == "Bob")
        assert( password == "password_of_bob")
    }
}