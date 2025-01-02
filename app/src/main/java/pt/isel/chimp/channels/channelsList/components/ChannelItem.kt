package pt.isel.chimp.channels.channelsList.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.UserParcelable
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.channels.generalComponents.ChannelLogo
import pt.isel.chimp.domain.Role
import pt.isel.chimp.utils.RoundedRectangleWithText


@Composable
fun ChannelItem(
    channel: Channel,
    role: Role,
    onClick: (ChannelParcelable) -> Unit,
    showJoinMessage: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                onClick(
                    ChannelParcelable(
                        channel.id,
                        channel.name,
                        UserParcelable(channel.creator.id, channel.creator.username, channel.creator.email),
                        channel.visibility,
                        role)
                )
                       },
    ) {
        Column {
            ChannelDetailsView(channel)
            if (showJoinMessage) {
                Text(
                    text = "You can join this channel",
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ChannelDetailsView(
    channel: Channel
) {
    Row(
        modifier = Modifier.padding(10.dp).fillMaxWidth().fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ChannelLogo(name = channel.name)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.widthIn(min = 0.dp, max = 150.dp)
        ) {
            Text(
                text = channel.name, fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        )
        {
            RoundedRectangleWithText(
                text = channel.visibility.name,
                backgroundColor = Color.DarkGray
            )
        }
    }
}




@Preview
@Composable
fun ChannelItemPreview() {
    ChannelItem(
        channel = Channel(
            1, "Channel 1 long name",
            User(1, "Bob", "bob@mexample.com"),
            Visibility.PUBLIC
        ),
        onClick = {

        },
        role = Role.READ_ONLY
    )
}

