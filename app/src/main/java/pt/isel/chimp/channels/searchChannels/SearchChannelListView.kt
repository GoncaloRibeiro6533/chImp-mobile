package pt.isel.chimp.channels.searchChannels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.channelsList.components.ChannelItem
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

@Composable
fun SearchChannelListView(
    channels: List<Channel>,
    userChannels: List<Channel>,
    currentUser: User,
    viewModel: SearchChannelsViewModel,
    onChannelSelected: (ChannelParcelable) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels) { channel ->
            val isUserInChannel = userChannels.any { it.id == channel.id }
            ChannelItem(
                channel = channel,
                role = Role.READ_WRITE,
                onClick = { ch ->
                    if (!isUserInChannel && channel.visibility == Visibility.PUBLIC) {
                        viewModel.addUserToChannel(currentUser.id, channel.id)
                    }
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