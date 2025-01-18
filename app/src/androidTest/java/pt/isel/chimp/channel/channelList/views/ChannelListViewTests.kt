package pt.isel.chimp.channel.channelList.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.channels.channelsList.CHANNEL_LIST_TEST_TAG
import pt.isel.chimp.channels.channelsList.ChannelListView
import pt.isel.chimp.channels.channelsList.NO_CHANNELS_TAG
import pt.isel.chimp.channels.channelsList.components.CHANNEL_ITEM_TEST_TAG
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User


@RunWith(AndroidJUnit4::class)
class ChannelListViewTests {

    @get:Rule
    val composeTree = createComposeRule()


    @Test
    fun channel_list_display_no_channels_if_list_is_empty(){
        val list = MutableStateFlow(emptyMap<Channel, Role>())
        composeTree.setContent {
            ChannelListView(
                channels = list,
                onChannelSelected = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(CHANNEL_LIST_TEST_TAG)
            .assertIsDisplayed()
        composeTree
            .onNodeWithTag(NO_CHANNELS_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun channel_list_display_channels(){
        val creator = User(1, "Alice", "alice@example.com")
        val list = MutableStateFlow(mapOf(
            Channel(1, "Channel 1", creator, Visibility.PUBLIC) to Role.READ_WRITE,
        ))
        composeTree.setContent {
            ChannelListView(
                channels = list,
                onChannelSelected = { /* no-op */ },
            )
        }
        composeTree
            .onNodeWithTag(CHANNEL_LIST_TEST_TAG)
            .assertIsDisplayed()
        composeTree
            .onNodeWithTag(NO_CHANNELS_TAG)
            .assertDoesNotExist()
        composeTree
            .onNodeWithTag(CHANNEL_ITEM_TEST_TAG)
            .assertIsDisplayed()
    }
}