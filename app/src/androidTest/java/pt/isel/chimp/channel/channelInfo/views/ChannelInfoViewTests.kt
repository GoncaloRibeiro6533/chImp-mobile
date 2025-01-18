package pt.isel.chimp.channel.channelInfo.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.channels.channelInfo.ADD_MEMBER_BUTTON
import pt.isel.chimp.channels.channelInfo.CHANNELS
import pt.isel.chimp.channels.channelInfo.CHANNEL_LIST
import pt.isel.chimp.channels.channelInfo.ChannelInfo
import pt.isel.chimp.channels.channelInfo.ChannelInfoView
import pt.isel.chimp.channels.channelInfo.EDIT_CHANNEL_BUTTON
import pt.isel.chimp.channels.channelInfo.LEAVE_CHANNEL_BUTTON
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User


@RunWith(AndroidJUnit4::class)
class ChannelInfoViewTests {

    @get:Rule
    val composeTree = createComposeRule()

    val user1 = User(1, "Bob", "bob@example.com")
    val user2 = User(2, "Alice", "bob@example.com")
    val user3 = User(3, "Charlie", "bob@example.com")
    val list = listOf(
        Pair(user1, Role.READ_WRITE),
        Pair(user2, Role.READ_WRITE),
        Pair(user3, Role.READ_ONLY)
    )

    val channel = Channel(1, "Channel 1 long", user1, Visibility.PRIVATE)


    @Test
    fun testChannelInfoView_display_all_items() {

        composeTree.setContent {
            ChannelInfoView(
                channelInfo = ChannelInfo(user1, channel, list),
                onUpdateChannel = { _ -> },
                onLeaveChannel = { },
                onInviteMember = { },
            )
        }
        composeTree.onNodeWithTag(CHANNEL_LIST).assertIsDisplayed()
        composeTree.onNodeWithTag(ADD_MEMBER_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(EDIT_CHANNEL_BUTTON).assertIsDisplayed()
        composeTree.onNodeWithTag(CHANNELS).assertIsDisplayed()
        composeTree.onNodeWithTag(LEAVE_CHANNEL_BUTTON).assertIsDisplayed()
    }
}