package pt.isel.chimp.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R
import pt.isel.chimp.ui.theme.ChImpTheme


const val ERROR_ALERT = "error_alert"
const val ERROR_ALERT_TITLE = "error_title"
const val ERROR_ALERT_MESSAGE = "error_message"
const val ERROR_DISMISS_BUTTON = "error_dismiss_button"

@Composable
fun ErrorAlert(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag(ERROR_ALERT),
        onDismissRequest = { },
        confirmButton = {
            OutlinedButton(
                border = BorderStroke(0.dp, Color.Unspecified),
                onClick = onDismiss,
                modifier = Modifier.testTag(ERROR_DISMISS_BUTTON)
            ) {
                Text(
                    text = buttonText
                )
            }
        },
        title = { Text(
            text = title,
            modifier = Modifier.testTag(ERROR_ALERT_TITLE)
        ) },
        text = { Text(
            text = message,
            modifier = Modifier.testTag(ERROR_ALERT_MESSAGE)
        ) },
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(id = R.string.error_icon_description)
            )
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorAlertPreview() {
    ChImpTheme {
        ErrorAlert(
            title = "Error",
            message = "An error occurred",
            buttonText = "OK",
            onDismiss = { }
        )
    }
}
