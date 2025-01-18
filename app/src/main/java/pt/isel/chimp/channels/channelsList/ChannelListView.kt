package pt.isel.chimp.channels.channelsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.channelsList.components.ChannelItem
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

const val CHANNEL_LIST_TEST_TAG = "ChannelList"
const val NO_CHANNELS_TAG = "NoChannels"

@Composable
fun ChannelListView(
    channels: StateFlow<Map<Channel,Role>>,
    onChannelSelected: (ChannelParcelable) -> Unit
) {
    val channelsState = channels.collectAsState().value
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize().testTag(CHANNEL_LIST_TEST_TAG)
    ) {
        items(channelsState.keys.toList()) { channel ->
            ChannelItem(
                channel = channel,
                role = channelsState[channel] ?: Role.READ_WRITE,
                onClick = { ch ->
                    onChannelSelected(ch)
                })
        }
        if (channelsState.isEmpty()) {
            item {
                Text(
                    text = "You don't have any channels yet",
                    modifier = Modifier.padding(16.dp).testTag(NO_CHANNELS_TAG)
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChannelsList() {
    val fakeChannels = mapOf(
        Channel(1, "Channel 1", User(1, "Alice", "alice@example.com"), Visibility.PUBLIC) to Role.READ_WRITE,
        Channel(2, "Channel 2", User(2, "Bob", "bob@example.com"), Visibility.PRIVATE) to Role.READ_ONLY,
    )

    val channelsFlow = MutableStateFlow<Map<Channel, Role>>(fakeChannels)
    ChannelListView(channelsFlow) { }
}