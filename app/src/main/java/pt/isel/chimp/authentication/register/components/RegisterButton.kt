package pt.isel.chimp.authentication.register.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R
import pt.isel.chimp.authentication.components.IconButton

private const val BUTTON_PADDING = 8

/**
 * Button for register operation.
 *
 * @param enabled whether the button is enabled or not
 * @param onRegisterClickCallback callback to be invoked when the register button is clicked
 */
@Composable
fun RegisterButton(
    enabled: Boolean = true,
    onRegisterClickCallback: () -> Unit
) {
    IconButton(
        onClick = onRegisterClickCallback,
        enabled = enabled,
        modifier = Modifier.padding(BUTTON_PADDING.dp),
        text = "Register",
        painter = painterResource(id = R.drawable.ic_round_person_add_24),
        contentDescription = "Button to register screen"
    )
}
