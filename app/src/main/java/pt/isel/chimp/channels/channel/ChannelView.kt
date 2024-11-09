package pt.isel.chimp.channels.channel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

@Composable
fun ChannelView(
    messages: List<Message>,
    onMessageSend: (String) -> Unit) {
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
                .fillMaxWidth()
        ) { // TODO hide this according to the user role
            ChatBoxView(onMessageSend = {  content ->
                onMessageSend(content)
            })
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
        )
    ) { }
}