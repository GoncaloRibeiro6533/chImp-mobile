package pt.isel.chimp.channels.channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.message.MessageView
import pt.isel.chimp.ui.theme.ChImpTheme
import java.time.LocalDateTime


@Composable
fun MessagesView(messages: List<Message>) {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(messages) { message ->
                MessageView(
                    user = message.sender,
                    message = message.content,
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
fun ChannelMessagesView() {
    ChImpTheme {
        MessagesView(
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
                ),
                Message(
                    3,
                    sender = userBob,
                    channel = channel,
                    content = "How are you?",
                    timestamp = LocalDateTime.of(2021, 10, 1, 10, 2)
                ),
                Message(
                    4,
                    sender = userAlice,
                    channel = channel,
                    content = "I'm fine, thanks!",
                    timestamp = LocalDateTime.of(2021, 10, 1, 10, 3)
                ),
            ))
    }
}