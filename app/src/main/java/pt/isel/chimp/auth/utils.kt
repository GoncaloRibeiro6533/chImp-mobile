package pt.isel.chimp.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


const val MIN_USERNAME_LENGTH = 3
const val MAX_USERNAME_LENGTH = 40

const val MIN_PASSWORD_LENGTH = 4
const val MAX_PASSWORD_LENGTH = 127

const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"


@Composable
fun AuthenticationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    maxLength: Int? = null,
    forbiddenCharacters: List<Char> = emptyList(),
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        label = {
            Text(
                text = "$label${if (required) " *" else ""}" +
                        if (errorMessage != null) " - $errorMessage" else ""
            )
        },
        value = value,
        onValueChange = {
            var newValue = it

            forbiddenCharacters.forEach { forbiddenChar ->
                newValue = newValue.replace(forbiddenChar.toString(), "")
            }

            if (maxLength != null && newValue.length > maxLength)
                newValue = newValue.substring(0 until maxLength)

            onValueChange(newValue)
        },
        singleLine = true,
        modifier = modifier,
        isError = errorMessage != null,
        visualTransformation = visualTransformation
    )
}
/*
@Composable
fun UsernameTextField(
    username: String,
    onUsernameChangeCallback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val invalidUsernameMessage = "Username shoudl have atleast 3 characters."
    val invalidUsername = username.isNotEmpty() && !validateUsername(username)

    AuthenticationTextField(
        label = "Username",
        value = username,
        onValueChange = onUsernameChangeCallback,
        modifier = Modifier.fillMaxWidth().then(modifier),
        required = true,
        maxLength = MAX_USERNAME_LENGTH,
        forbiddenCharacters = listOf('\n'),
        errorMessage = if (invalidUsername) invalidUsernameMessage else null
    )
}

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

 */