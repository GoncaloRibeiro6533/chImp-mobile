package pt.isel.chimp.authentication.login.views

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.authentication.login.components.LOGIN_PASSWORD_TEXT_FIELD
import pt.isel.chimp.authentication.login.components.LOGIN_USERNAME_TEXT_FIELD
import pt.isel.chimp.authentication.login.components.LoginTextFields

@RunWith(AndroidJUnit4::class)
class LoginTextFieldsTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_textFields_are_displayed() {
        composeTestRule.setContent {
            LoginTextFields(
                username = "Bob",
                password = "password_of_bob",
                onUsernameChangeCallback = {},
                onPasswordChangeCallback = {}
            )
        }
        composeTestRule.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).assertExists()
        composeTestRule.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).assertExists()
    }

    /*@Test
    fun textFields_change_text_onUsernameChange_and_onPasswordChange_are_called() {
        var username = ""
        var password = ""
        composeTestRule.setContent {
            LoginTextFields(
                username = username,
                password = password,
                onUsernameChangeCallback = { username = it },
                onPasswordChangeCallback = { password = it }
            )
        }
        composeTestRule.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).performTextInput("Bob")
        composeTestRule.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).performTextInput("password_of_bob")
        composeTestRule.onNodeWithTag(LOGIN_USERNAME_TEXT_FIELD).assert(hasText("Bob"))
        composeTestRule.onNodeWithTag(LOGIN_PASSWORD_TEXT_FIELD).assert(hasText("password_of_bob"))
        assertEquals("Bob", username)
        assertEquals("password_of_bob", password)
    }*/
}