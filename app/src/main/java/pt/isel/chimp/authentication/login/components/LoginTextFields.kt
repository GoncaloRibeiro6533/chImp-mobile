package pt.isel.chimp.authentication.login.components

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

private const val USERNAME_TO_PASSWORD_PADDING = 8
private const val TEXT_FIELD_WIDTH_FACTOR = 0.6f



const val LOGIN_USERNAME_TEXT_FIELD = "login_username_text_fields"
const val LOGIN_PASSWORD_TEXT_FIELD = "login_password_text_fields"

/**
 * The text fields for the login operation on the login page:
 * - Username field
 * - Password field
 *
 * @param username username to show
 * @param password password to show
 * @param onUsernameChangeCallback callback to be invoked when the username text is changed
 * @param onPasswordChangeCallback callback to be invoked when the password text is changed
 */
@Composable
fun LoginTextFields(
    username: String,
    password: String,
    onUsernameChangeCallback: (String) -> Unit,
    onPasswordChangeCallback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(TEXT_FIELD_WIDTH_FACTOR)) {
        UsernameTextField(
            username = username,
            onUsernameChangeCallback = onUsernameChangeCallback,
            modifier = Modifier.padding(bottom = USERNAME_TO_PASSWORD_PADDING.dp).testTag(LOGIN_USERNAME_TEXT_FIELD)
        )
        PasswordTextField(
            password = password,
            onPasswordChangeCallback = onPasswordChangeCallback,
            modifier = Modifier.testTag(LOGIN_PASSWORD_TEXT_FIELD)
        )
    }
}

@Preview
@Composable
fun LoginTextFieldsPreview() {
    LoginTextFields(
        username = "Bob",
        password = "password_of_bob",
        onUsernameChangeCallback = {},
        onPasswordChangeCallback = {}
    )
}