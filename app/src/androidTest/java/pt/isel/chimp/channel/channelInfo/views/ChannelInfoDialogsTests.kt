package pt.isel.chimp.channel.channelInfo.views

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.channels.channelInfo.CANCEL_BUTTON
import pt.isel.chimp.channels.channelInfo.ChannelDialog
import pt.isel.chimp.channels.channelInfo.DIALOG_BUTTON
import pt.isel.chimp.channels.channelInfo.CONFIRM_BUTTON
import pt.isel.chimp.channels.channelInfo.DIALOG_TEXT_FIELD
import pt.isel.chimp.channels.channelInfo.DROPDOWN_BUTTON

@RunWith(AndroidJUnit4::class)
class ChannelInfoDialogsTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testChannelInfoDialog() {
        composeTestRule.setContent {
            ChannelDialog(
                buttonName = "",
                message = "",
                confirmMessage = "",
                placeHolderText = "",
                buttonColor = Color.Black,
                buttonTextColor = Color.Black,
                onConfirm = {},
                modifier = Modifier

            )
        }
        composeTestRule.onNodeWithTag(DIALOG_BUTTON)
        composeTestRule.onNodeWithTag(DIALOG_TEXT_FIELD)
        composeTestRule.onNodeWithTag(DROPDOWN_BUTTON)
        composeTestRule.onNodeWithTag(CANCEL_BUTTON)
        composeTestRule.onNodeWithTag(CONFIRM_BUTTON)


    }

}