package pt.isel.chimp.loading

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.components.PROGRESS_INDICATOR_TAG

@RunWith(AndroidJUnit4::class)
class LoadingViewTests {

    @get:Rule
    val composeTree = createComposeRule()
    //TODO
    @Test
    fun progressIndicator_is_shown() {
        composeTree.setContent {
            LoadingView()
        }
        composeTree.onNodeWithTag(PROGRESS_INDICATOR_TAG).assertIsDisplayed()
    }
}