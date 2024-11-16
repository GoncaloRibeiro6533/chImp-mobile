package pt.isel.chimp.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTests {

    @get:Rule
    val composeTree = createComposeRule()
    /* TODO
    @Test
    fun when_loading_state_loading_view_is_displayed() {
        composeTree.setContent {
            ProfileScreen(
                viewModel = ProfileScreenViewModel(),
                onNavigateBack = { /* no-op */ }
            )
        }
        composeTree.onNodeWithTag("loading_view").assertIsDisplayed()
    }*/
}