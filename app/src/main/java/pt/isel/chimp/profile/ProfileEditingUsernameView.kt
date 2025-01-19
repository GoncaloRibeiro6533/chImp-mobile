package pt.isel.chimp.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.ui.theme.ChImpTheme

const val EDIT_USERNAME_VIEW_TAG = "EditingUsernameView"
const val SAVE_BUTTON_TAG = "SaveButton"
const val CANCEL_BUTTON_TAG = "CancelButton"

@Composable
fun EditingUsernameView(
    state : ProfileScreenState.EditingUsername,
    onSaveIntent: (String) -> Unit,
    onCancelIntent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).testTag(EDIT_USERNAME_VIEW_TAG),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var currentUsername by rememberSaveable { mutableStateOf(state.profile.username) }
        TextField(
            value = currentUsername,
            onValueChange = { currentUsername = it.trim() },
            singleLine = true,
            supportingText = {
                Text(text = stringResource(R.string.username_label))
            },
            modifier = Modifier.testTag(USERNAME_TEXT_TAG)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Button(
                modifier = modifier.testTag(CANCEL_BUTTON_TAG),
                onClick = {
                    onCancelIntent() },
            ) {
                Text(text = stringResource(R.string.editing_username_cancel_button))
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Button(
                modifier = modifier.testTag(SAVE_BUTTON_TAG),
                onClick = { onSaveIntent(currentUsername) },
                enabled = currentUsername != state.profile.username
                        && currentUsername.isNotBlank()
                        && currentUsername.length <= User.MAX_USERNAME_LENGTH


            ) {
                Text(text = stringResource(R.string.editing_username_save_button))
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfigurationViewPreview() {
    ChImpTheme {
        EditingUsernameView (
            state = ProfileScreenState.EditingUsername(Profile("Bob", "bob@example.com")),
            onSaveIntent = {},
            onCancelIntent = {}
        )
    }
}