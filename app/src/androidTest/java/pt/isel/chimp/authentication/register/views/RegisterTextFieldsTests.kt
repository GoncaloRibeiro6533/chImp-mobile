package pt.isel.chimp.authentication.register.views

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.authentication.register.components.REGISTER_EMAIL_TEXT_FIELD
import pt.isel.chimp.authentication.register.components.REGISTER_PASSWORD_TEXT_FIELD
import pt.isel.chimp.authentication.register.components.REGISTER_USERNAME_TEXT_FIELD
import pt.isel.chimp.authentication.register.components.RegisterTextFields

@RunWith(AndroidJUnit4::class)
class RegisterTextFieldsTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun register_textFields_are_displayed() {
        composeTestRule.setContent {
            RegisterTextFields(
                email = "",
                username = "",
                password = "",
                onEmailChangeCallback = {},
                onUsernameChangeCallback = {},
                onPasswordChangeCallback = {}

            )
        }
        composeTestRule.onNodeWithTag(REGISTER_EMAIL_TEXT_FIELD)
        composeTestRule.onNodeWithTag(REGISTER_USERNAME_TEXT_FIELD)
        composeTestRule.onNodeWithTag(REGISTER_PASSWORD_TEXT_FIELD)
    }

}