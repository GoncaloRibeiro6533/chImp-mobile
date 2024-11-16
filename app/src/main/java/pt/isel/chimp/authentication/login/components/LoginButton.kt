package pt.isel.chimp.authentication.login.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R
import pt.isel.chimp.authentication.components.IconButton


private const val BUTTON_PADDING = 16

/**
 * Button for login operation.
 *
 * @param enabled whether the button is enabled or not
 * @param onLoginClickCallback callback to be invoked when the login button is clicked
 */
@Composable
fun LoginButton(
    enabled: Boolean = true,
    onLoginClickCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onLoginClickCallback,
        enabled = enabled,
        modifier = modifier.padding(BUTTON_PADDING.dp),
        text = stringResource(id = R.string.login_button_text),
        painter = painterResource(id = R.drawable.ic_round_login_24),
        contentDescription = stringResource(id = R.string.login_button_content_description)
    )
}
