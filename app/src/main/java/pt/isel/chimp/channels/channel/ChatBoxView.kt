package pt.isel.chimp.channels.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.chimp.ui.theme.ChImpTheme


@Composable
fun ChatBoxView(
    onMessageSend: (String) -> Unit = {},
    enabled: Boolean = true
) {
    if(!enabled) {
        return
    }
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Box (
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart),
            verticalAlignment = Alignment.Bottom,
        ) {
            TextField(
                value = chatBoxValue,
                onValueChange = { newText ->
                    chatBoxValue = newText
                },
                placeholder = {
                    Text(text = "Send message to channel") // TODO add channel name as bold
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                minLines = 1,
                maxLines = 5
            )
            IconButton(
                onClick = {
                    if (chatBoxValue.text.isNotBlank()) {
                        onMessageSend(chatBoxValue.text.trimEnd())
                        chatBoxValue = TextFieldValue("")
                    }
                },
                modifier = Modifier.align(Alignment.Bottom)
                ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Message to Channel")

            }
        }
    }
}


@Preview
@Composable
fun ChatBoxViewPreview() {
    ChImpTheme {
         ChatBoxView({}, true)
    }
}