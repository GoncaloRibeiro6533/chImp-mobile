package pt.isel.chimp.authentication.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.authentication.components.PasswordTextField
import pt.isel.chimp.authentication.components.UsernameTextField

private const val EMAIL_TO_USERNAME_PADDING = 8
private const val USERNAME_TO_PASSWORD_PADDING = 8
private const val TEXT_FIELD_WIDTH_FACTOR = 0.6f

const val REGISTER_USERNAME_TEXT_FIELD = "register_username_text_fields"
const val REGISTER_PASSWORD_TEXT_FIELD = "register_password_text_fields"
const val REGISTER_EMAIL_TEXT_FIELD = "register_email_text_fields"

/**
 * The text fields for the register operation on the register page:
 * - Email field
 * - Username field
 * - Password field
 *
 * @param email email to show
 * @param username username to show
 * @param password password to show
 * @param onEmailChangeCallback callback to be invoked when the email text is changed
 * @param onUsernameChangeCallback callback to be invoked when the username text is changed
 * @param onPasswordChangeCallback callback to be invoked when the password text is changed
 */
@Composable
fun RegisterTextFields(
    email: String,
    username: String,
    password: String,
    onEmailChangeCallback: (String) -> Unit,
    onUsernameChangeCallback: (String) -> Unit,
    onPasswordChangeCallback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(TEXT_FIELD_WIDTH_FACTOR)) {
        EmailTextField(
            email = email,
            onEmailChangeCallback = onEmailChangeCallback,
            modifier = Modifier.padding(bottom = EMAIL_TO_USERNAME_PADDING.dp).fillMaxWidth().testTag(REGISTER_EMAIL_TEXT_FIELD)
        )

        UsernameTextField(
            username = username,
            onUsernameChangeCallback = onUsernameChangeCallback,
            modifier = Modifier.padding(bottom = USERNAME_TO_PASSWORD_PADDING.dp).fillMaxWidth().testTag(REGISTER_USERNAME_TEXT_FIELD)
        )

        PasswordTextField(
            password = password,
            onPasswordChangeCallback = onPasswordChangeCallback,
            modifier = Modifier.testTag(REGISTER_PASSWORD_TEXT_FIELD)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterTextFieldsPreview() {
    RegisterTextFields("", "", "", {}, {}, {})
}
