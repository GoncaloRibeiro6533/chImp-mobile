package pt.isel.chimp.authentication.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import pt.isel.chimp.authentication.MAX_PASSWORD_LENGTH
import pt.isel.chimp.authentication.validatePassword

/**
 * The password text field.
 *
 * @param password password to show
 * @param onPasswordChangeCallback callback to be invoked when the password text is changed
 */
@Composable
fun PasswordTextField(
    password: String,
    onPasswordChangeCallback: (String) -> Unit
) {
    val invalidPasswordMessage = "Password should have at least 8 characters."
    val invalidPassword = password.isNotEmpty() && !validatePassword(password)

    AuthenticationTextField(
        label = "Password",
        value = password,
        onValueChange = onPasswordChangeCallback,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        required = true,
        maxLength = MAX_PASSWORD_LENGTH,
        errorMessage = if (invalidPassword) invalidPasswordMessage else null
    )
}
