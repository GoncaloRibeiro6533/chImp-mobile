package pt.isel.chimp.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.chimp.ui.theme.ChImpTheme


@Composable
fun EditingUsernameView(
    username: String,
    onSaveIntent: (String) -> Unit,
    onCancelIntent: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var currentUsername by remember { mutableStateOf(username) }
        TextField(
            value = currentUsername,
            onValueChange = { currentUsername = it.trim() },
            singleLine = true,
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Button(
                onClick = {
                    onCancelIntent() },
            ) {
                Text(text = "cancel")
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Button(
                onClick = { onSaveIntent(currentUsername) },
                enabled = currentUsername != username
            ) {
                Text(text = "save")
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfigurationViewPreview() {
    ChImpTheme {
        EditingUsernameView (
            username = "username",
            onSaveIntent = {},
            onCancelIntent = {}
        )
    }
}