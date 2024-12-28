package pt.isel.chimp.channels.channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.message.MessageView
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.RoundedRectangleWithText
import java.time.LocalDateTime


@Composable
fun MessagesView(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    loadingMore: Boolean = false,
    loadedAll: Boolean = false,
    loadMore: (Int) -> Unit = {}
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages, loadingMore, loadedAll) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex != null && lastVisibleItemIndex >= messages.size - 5 &&
                    !loadingMore && !loadedAll
                ) {
                    loadMore(messages.size)
                }
            }
    }

    if (messages.isEmpty() && !loadingMore) {
        EmptyConversationMessage()
    } else {
        Column {
        LazyColumn(
            modifier = modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            state = listState,
            reverseLayout = true,
        ) {
            itemsIndexed(messages) { index, message ->
                MessageView(user = message.sender, message = message)
                if (shouldShowDateSeparator(messages, index)) {
                    DateSeparator(message.timestamp)
                }
                if (index == messages.lastIndex) {
                    FooterMessage(
                        isLoading = loadingMore,
                        showStartMessage = messages.size < 40 || loadedAll
                    )
                }
            }
        }
        }
    }
}



@Composable
fun EmptyConversationMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        RoundedRectangleWithText("Start of conversation", backgroundColor = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DateSeparator(timestamp: LocalDateTime) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        RoundedRectangleWithText(
            text = getDay(timestamp),
            backgroundColor = Color.Black
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
fun FooterMessage(isLoading: Boolean, showStartMessage: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            showStartMessage -> {
                RoundedRectangleWithText("Start of conversation", backgroundColor = Color.Black)
            }
        }
    }
}

fun shouldShowDateSeparator(messages: List<Message>, index: Int): Boolean {
    return when {
        index == messages.lastIndex -> true
        index < messages.lastIndex -> {
            !compareDays(
                messages[index].timestamp,
                messages[index + 1].timestamp
            )
        }
        else -> false
    }
}



private fun compareDays(date1: LocalDateTime, date2: LocalDateTime): Boolean {
    return date1.dayOfYear == date2.dayOfYear && date1.year == date2.year
}

private fun getDay(date: LocalDateTime): String {
    return "${date.dayOfMonth}/${date.monthValue}/${date.year}"
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
                    2,
                    sender = userAlice,
                    channel = channel,
                    content = "Hello, Bob!",
                    timestamp = LocalDateTime.of(2021, 10, 1, 10, 1)
                ),
                Message(
                    1,
                    sender = userBob,
                    channel = channel,
                    content = "Hello, Alice!",
                    timestamp = LocalDateTime.of(2021, 10, 1, 10, 0)
                ),
            ))
    }
}