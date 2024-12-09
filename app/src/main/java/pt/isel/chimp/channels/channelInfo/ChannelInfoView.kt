package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.channels.generalComponents.ChannelLogo
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
    onUpdateChannel : (String) -> Unit,
    onLeaveChannel : () -> Unit
){


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .testTag(CHANNEL_LIST),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChannelLogo(channelInfo.channel.name, 135)
        Text(
            text = channelInfo.channel.name,
            modifier = Modifier.padding(10.dp),
            fontSize = 30.sp
        )
        Text(
            text = "Channel - ${channelInfo.members.size} members",
            modifier = Modifier.padding(6.dp)
        )

        Row (
            horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),

            ) {

            ChannelDialog(
                "Invite Member +",
                "Enter Member Username:",
                "Invite",
                "username",
                Color(0xFF32cd32),
                Color.Black,
                { TODO() },
                Modifier.testTag(ADD_MEMBER_BUTTON)
            )

            ChannelDialog(
                "Edit Channel Name",
                "Enter new channel name:",
                "OK",
                channelInfo.channel.name,
                Color.LightGray,
                Color.Black,
                { newName -> onUpdateChannel(newName)},
                Modifier.testTag(EDIT_CHANNEL_BUTTON)
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().testTag(CHANNELS)
        ) {
            items(channelInfo.members) { member ->
                MemberView(member.first, member.second, channelInfo.channel.creator)
            }
            item {

                ChannelDialog(
                    "Leave Channel",
                    "Do you want to leave this Channel?",
                    "Leave", "",
                    Color.Red,
                    Color.White,
                    { onLeaveChannel() },
                    Modifier.testTag(LEAVE_CHANNEL_BUTTON)
                )


                /*
                Button(
                    onClick = {
                        viewModel.leaveChannel(token, channel, user)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonColors(Color.Red, Color.White, Color.Green, Color.Green)
                ) {
                    Text("Leave Group")


                }*/
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
    val list = listOf(
        Pair(user1, Role.READ_WRITE),
        Pair(user2, Role.READ_WRITE),
        Pair(user3, Role.READ_ONLY)
    )

    val channel = Channel(1, "Channel 1 long", user1, Visibility.PUBLIC)
    ChannelInfoView(
        channelInfo = ChannelInfo(channel, list),
        onUpdateChannel = { },
        onLeaveChannel = { }
    )
}

