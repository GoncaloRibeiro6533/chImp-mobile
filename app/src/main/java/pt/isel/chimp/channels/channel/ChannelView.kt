package pt.isel.chimp.channels.channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

@Composable
fun ChannelView(
    messages: StateFlow<List<Message>>,
    onMessageSend: (String) -> Unit,
    userRole: Role
) {
    val messagesList = messages.collectAsState().value
    Column(
        modifier = Modifier.fillMaxSize().fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            MessagesView(
                messagesList,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth().heightIn(min = 100.dp).shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (userRole == Role.READ_WRITE) {
                ChatBoxView(onMessageSend = { content ->
                    onMessageSend(content)
                }, enabled = true)
            }
            else {
                ChatBoxView( enabled = false)
                Text(
                    text = "You don't have permission to send messages"
                    )
            }
        }
    }
}



private val userBob = User(1, "Bob", "bob@example.com")
private val  userAlice = User(2, "Alice", "alice@example.com")
private val channel = Channel(1, "Channel 1", userBob, Visibility.PUBLIC)

private val messages =
        listOf(
            Message(
                1,
                sender = userBob,
                channel = channel,
                content = "Hello, Alice!",
                timestamp = LocalDateTime.of(2021, 10, 1, 10, 0)
            ),
            Message(
                2,
                sender = userAlice,
                channel = channel,
                content = "Hello, Bob!",
                timestamp = LocalDateTime.of(2021, 10, 1, 10, 1)
            )
        )

@Preview(showBackground = true)
@Composable
fun ChannelViewPreview() {
    ChannelView(
        messages = MutableStateFlow(messages),
        onMessageSend = { },
        userRole = Role.READ_WRITE
    )
}


@Preview(showBackground = true)
@Composable
fun ChannelViewPreviewReadOnly() {
    ChannelView(
        messages = MutableStateFlow(messages),
        onMessageSend = { },
        userRole = Role.READ_ONLY
    )
}