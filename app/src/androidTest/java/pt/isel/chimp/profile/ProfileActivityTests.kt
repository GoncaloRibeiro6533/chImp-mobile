package pt.isel.chimp.profile

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import pt.isel.chimp.ui.NavigateBackTestTag
// TODO does not work
/*
class ProfileActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<ProfileActivity>()

    @Test
    fun when_activity_is_launched_then_ProfileView_is_displayed() {
        testRule.onNodeWithTag(PROFILE_VIEW_TAG).assertExists()
    }

    @Test
    fun when_pressing_back_button_then_activity_is_finished() {
        testRule.onNodeWithTag(NavigateBackTestTag).performClick()
        assert(testRule.activity.isFinishing)
    }

}*/