package pt.isel.chimp.authentication.register.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.isel.chimp.authentication.components.AuthenticationTextField
import pt.isel.chimp.authentication.validateEmail

/**
 * The email text field.
 *
 * @param email email to show
 * @param onEmailChangeCallback callback to be invoked when the email text is changed
 * @param modifier modifier to be applied to the text field
 */
@Composable
fun EmailTextField(
    email: String,
    onEmailChangeCallback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val invalidEmailMessage = "Invalid email"
    val invalidEmail = email.isNotEmpty() && !validateEmail(email)

    AuthenticationTextField(
        label = "Email",
        value = email,
        onValueChange = onEmailChangeCallback,
        modifier = Modifier.fillMaxWidth().then(modifier),
        required = true,
        errorMessage = if (invalidEmail) invalidEmailMessage else null
    )
}
