package pt.isel.chimp.channels.channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import java.time.LocalDateTime

@Composable
fun ChannelView(
    messages: List<Message>,
    onMessageSend: (String) -> Unit,
    userRole: UserInChannel
) {
    Column(
        modifier = Modifier.fillMaxSize().fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            MessagesView(
                messages,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth().heightIn(min = 100.dp).shadow(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (userRole.role == Role.READ_WRITE) {
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


@Preview(showBackground = true)
@Composable
fun ChannelViewPreview() {
    ChannelView(
        messages = listOf(
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
        ),
        onMessageSend = { },
        userRole = UserInChannel(userBob.id, channel.id, Role.READ_WRITE)
    )
}

@Preview(showBackground = true)
@Composable
fun ChannelViewPreviewReadOnly() {
    ChannelView(
        messages = listOf(
            Message(
                1,
                sender = userBob,
                channel = channel,
                content = "Hello, Alice!",
                timestamp = LocalDateTime.of(2021, 10, 1, 10, 0)
            ),
            Message(
                2,
                sender = userBob,
                channel = channel,
                content = "This is my Channel",
                timestamp = LocalDateTime.of(2021, 10, 1, 10, 1)
            )
        ),
        onMessageSend = { },
        userRole = UserInChannel(userAlice.id, channel.id, Role.READ_ONLY)
    )
}