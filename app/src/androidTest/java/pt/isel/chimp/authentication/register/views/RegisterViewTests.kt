package pt.isel.chimp.authentication.register.views


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith


import pt.isel.chimp.authentication.register.REGISTER_BUTTON
import pt.isel.chimp.authentication.register.REGISTER_TEXT_FIELDS
import pt.isel.chimp.authentication.register.REGISTER_VIEW
import pt.isel.chimp.authentication.register.RegisterView
import pt.isel.chimp.authentication.register.components.REGISTER_EMAIL_TEXT_FIELD
import pt.isel.chimp.authentication.register.components.REGISTER_PASSWORD_TEXT_FIELD
import pt.isel.chimp.authentication.register.components.REGISTER_USERNAME_TEXT_FIELD



@RunWith(AndroidJUnit4::class)
class RegisterViewTests {

    @get:Rule
    val composeTree = createComposeRule()

    @Test
    fun testRegisterView() {
        composeTree.setContent {
            RegisterView(
                onSubmit = { _, _, _ -> },
            )
        }
        composeTree.onNodeWithTag(REGISTER_VIEW).assertIsDisplayed()
        composeTree.onNodeWithTag(REGISTER_TEXT_FIELDS).assertIsDisplayed()
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsDisplayed()
    }

    @Test
    fun testRegisterView_button_is_disabled_until_fields_are_valid() {
        composeTree.setContent {
            RegisterView(
                onSubmit = { _, _, _ -> },
            )
        }
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_EMAIL_TEXT_FIELD).performTextInput("alice@mail.com")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_USERNAME_TEXT_FIELD).performTextInput("Alice")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_PASSWORD_TEXT_FIELD).performTextInput("password_of_alice")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsEnabled()
    }

    @Test
    fun testRegisterView_button_is_disabled_until_username_has_minimum_length() {
        composeTree.setContent {
            RegisterView(
                onSubmit = { _, _, _ -> },
            )
        }
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_EMAIL_TEXT_FIELD).performTextInput("alice@mail.com")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_USERNAME_TEXT_FIELD).performTextInput("A")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_USERNAME_TEXT_FIELD).performTextInput("Alice")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_PASSWORD_TEXT_FIELD).performTextInput("password_of_alice")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsEnabled()
    }

    @Test
    fun
            testRegisterView_button_is_disabled_until_password_has_minimum_length() {
        composeTree.setContent {
            RegisterView(
                onSubmit = { _, _, _ -> },
            )
        }
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_EMAIL_TEXT_FIELD).performTextInput("alice@mail.com")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_USERNAME_TEXT_FIELD).performTextInput("Alice")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_PASSWORD_TEXT_FIELD).performTextInput("p")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsNotEnabled()
        composeTree.onNodeWithTag(REGISTER_PASSWORD_TEXT_FIELD).performTextInput("password_of_alice")
        composeTree.onNodeWithTag(REGISTER_BUTTON).assertIsEnabled()
    }

    @Test
    fun testRegisterView_onSubmit_action_is_called_when_register_button_clicked() {
        var submitted = false
        composeTree.setContent {
            RegisterView(
                onSubmit = { _, _, _ -> submitted = true },
            )
        }

        composeTree.onNodeWithTag(REGISTER_EMAIL_TEXT_FIELD).performTextInput("alice@mail.com")
        composeTree.onNodeWithTag(REGISTER_USERNAME_TEXT_FIELD).performTextInput("Alice")
        composeTree.onNodeWithTag(REGISTER_PASSWORD_TEXT_FIELD).performTextInput("password_of_alice")
        composeTree.onNodeWithTag(REGISTER_BUTTON).performClick()
        assert(submitted)
    }


}
