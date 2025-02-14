package pt.isel.chimp.ui.screens.channels.channelInfo

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import pt.isel.chimp.ui.screens.channels.generalComponents.ChannelLogo
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

const val  CHANNEL_LIST = "channel_list"
const val CHANNELS = "channels"
const val  ADD_MEMBER_BUTTON = "add_member_button"
const val  EDIT_CHANNEL_BUTTON = "edit_channel_button"
const val  LEAVE_CHANNEL_BUTTON = "leave_channel_button"


@Composable
fun ChannelInfoView(
    channelInfo: ChannelInfo,
    onUpdateChannel: (String) -> Unit,
    onLeaveChannel: () -> Unit,
    onInviteMember: () -> Unit
) {
    val orientation = LocalConfiguration.current.orientation

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        ChannelInfoPortraitLayout(
            channelInfo = channelInfo,
            onUpdateChannel = onUpdateChannel,
            onLeaveChannel = onLeaveChannel,
            onInviteMember = onInviteMember
        )
    } else {
        ChannelInfoLandscapeLayout(
            channelInfo = channelInfo,
            onUpdateChannel = onUpdateChannel,
            onLeaveChannel = onLeaveChannel,
            onInviteMember = onInviteMember
        )
    }
}

@Composable
fun ChannelInfoPortraitLayout(
    channelInfo: ChannelInfo,
    onUpdateChannel: (String) -> Unit,
    onLeaveChannel: () -> Unit,
    onInviteMember: () -> Unit
) {
    val members = channelInfo.members.collectAsState().value
    val channel = channelInfo.channel.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .testTag(CHANNEL_LIST),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChannelHeader(channelInfo, onUpdateChannel, onInviteMember)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .testTag(CHANNELS)
        ) {
            items(members.toList()) { member ->
                MemberView(member.first, member.second, channel.creator)
            }
            item {
                ChannelDialog(
                    "Leave Channel",
                    "Do you want to leave this Channel?",
                    "Leave",
                    "",
                    Color.Red,
                    Color.White,
                    { onLeaveChannel() },
                    Modifier.testTag(LEAVE_CHANNEL_BUTTON)
                )
            }
        }
    }
}

@Composable
fun ChannelInfoLandscapeLayout(
    channelInfo: ChannelInfo,
    onUpdateChannel: (String) -> Unit,
    onLeaveChannel: () -> Unit,
    onInviteMember: () -> Unit
) {
    val members = channelInfo.members.collectAsState().value
    val channel = channelInfo.channel.collectAsState().value
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag(CHANNEL_LIST),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ChannelHeader(channelInfo, onUpdateChannel, onInviteMember, 80, 25,0)
            ChannelDialog(
                "Leave Channel",
                "Do you want to leave this Channel?",
                "Leave",
                "",
                Color.Red,
                Color.White,
                { onLeaveChannel() },
                Modifier.testTag(LEAVE_CHANNEL_BUTTON)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .testTag(CHANNELS)
        ) {
            items(members.toList()) { member ->
                MemberView(member.first, member.second, channel.creator)
            }
        }
    }
}

@Composable
fun ChannelHeader(
    channelInfo: ChannelInfo,
    onUpdateChannel: (String) -> Unit,
    onInviteMember: () -> Unit,
    imageSize: Int = 135,
    fontSize: Int = 30,
    padding: Int = 10
) {
    val members = channelInfo.members.collectAsState().value
    val channel = channelInfo.channel.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChannelLogo(channel.name, imageSize)
        Text(
            text = channel.name,
            modifier = Modifier.padding(padding.dp),
            fontSize = fontSize.sp
        )
        Text(
            text = "Channel - ${members.size} members",
            modifier = Modifier.padding(6.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)
        ) {
            if (channel.visibility == Visibility.PRIVATE) {
                Button(
                    onClick = { onInviteMember() },
                    colors = ButtonColors(
                        containerColor = Color(0xFF32cd32),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0xFF32cd32),
                        disabledContentColor = Color.Black
                    ),
                    modifier = Modifier.testTag(ADD_MEMBER_BUTTON)
                ) {
                    Text("Invite Member +")
                }
            }
            if (channel.creator == channelInfo.user) {
                ChannelDialog(
                    "Edit Channel Name",
                    "Enter new channel name:",
                    "OK",
                    channel.name,
                    Color.LightGray,
                    Color.Black,
                    { newName -> onUpdateChannel(newName) },
                    Modifier.testTag(EDIT_CHANNEL_BUTTON)
                )
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelInfoViewPreview() {
    val user1 = User(1, "Bob", "bob@example.com")
    val user2 = User(2, "Alice", "bob@example.com")
    val user3 = User(3, "Charlie", "bob@example.com")
    val list = MutableStateFlow(mapOf(
        user1 to Role.READ_WRITE,
        user2 to Role.READ_WRITE,
        user3 to Role.READ_ONLY
    ))

    val channel = MutableStateFlow(Channel(1, "Channel 1 long", user1, Visibility.PUBLIC))
    ChannelInfoView(
        channelInfo = ChannelInfo(User(1,"Bob", "bob@example.com"), channel, list),
        onUpdateChannel = { },
        onLeaveChannel = { },
        onInviteMember = { }
    )
}

