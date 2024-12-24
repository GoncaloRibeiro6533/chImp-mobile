package pt.isel.chimp.channels.searchChannels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.channelsList.components.ChannelItem
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility

@Composable
fun SearchChannelListView(
    channels: List<Channel>,
    userChannels: StateFlow<Map<Channel, Role>>,
    onChannelSelected: (ChannelParcelable) -> Unit
) {
    val userC = userChannels.collectAsState().value
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels) { channel ->
            val isUserInChannel = userC.containsKey(channel)
            ChannelItem(
                channel = channel,
                role = Role.READ_WRITE,
                onClick = { ch ->
                    onChannelSelected(ch)
                },
                showJoinMessage = !isUserInChannel && channel.visibility == Visibility.PUBLIC
            )
        }
        if (channels.isEmpty()) {
            item {
                Text(
                    text = "You don't have any channels yet",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}