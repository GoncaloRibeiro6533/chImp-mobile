package pt.isel.chimp.channels.channelsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.channelsList.components.ChannelItem
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

@Composable
fun ChannelListView(
    channels: Map<Channel,Role>,
    onChannelSelected: (ChannelParcelable) -> Unit
) {

    LazyColumn(
        /*
        contentPadding = PaddingValues(
            top = 0.dp,
            bottom = innerPadding.calculateBottomPadding(),
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
        ),

         */
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(channels.keys.toList()) { channel ->
            ChannelItem(
                channel = channel,
                role = channels[channel] ?: Role.READ_ONLY, //TODO
                onClick = { channel ->
                    onChannelSelected(channel)
                })
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelListViewPreview() {
    val user1 = User(1, "Bob", "bob@example.com")

    val channel1 = Channel(1, "Channel 1", user1, Visibility.PUBLIC)
    val channel2 = Channel(2, "Channel 2", user1, Visibility.PUBLIC)
    val channel3 = Channel(3, "Channel 3", user1, Visibility.PUBLIC)
    val map = mapOf(
        channel1 to Role.READ_ONLY,
        channel2 to Role.READ_WRITE,
        channel3 to Role.READ_ONLY
    )


    ChannelListView(

        channels = map,
        onChannelSelected = {}
    )
}