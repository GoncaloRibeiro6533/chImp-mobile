package pt.isel.chimp.ui.screens.authentication.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.isel.chimp.ui.screens.authentication.MAX_USERNAME_LENGTH
import pt.isel.chimp.ui.screens.authentication.validateUsername

/**
 * The username text field.
 *
 * @param username username to show
 * @param onUsernameChangeCallback callback to be invoked when the username text is changed
 * @param modifier modifier to be applied to the text field
 */
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
        forbiddenCharacters = listOf(' ','\n', '\t'),
        errorMessage = if (invalidUsername) invalidUsernameMessage else null
    )
}
