package pt.isel.chimp.ui.screens.menu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.work.WorkManager
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.ui.screens.about.AboutActivity
import pt.isel.chimp.ui.screens.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.ui.screens.channels.createChannel.CreateChannelActivity
import pt.isel.chimp.ui.screens.channels.searchChannels.SearchChannelsActivity
import pt.isel.chimp.ui.screens.home.HomeActivity
import pt.isel.chimp.ui.screens.invitationList.InvitationListActivity
import pt.isel.chimp.ui.screens.profile.ProfileActivity
import pt.isel.chimp.utils.navigateTo

class MenuActivity : ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val chImpRepo by lazy { (application as DependenciesContainer).repo }
    private val viewModel by viewModels<MenuViewModel>(
        factoryProducer = {
            MenuViewModelFactory(
                userInfoRepository,
                chImpService,
                chImpRepo
            )
        }
    )

    private val menuItems = listOf(
        MenuItem("About", "about screen", Icons.Default.Info) {
            navigateTo(
                this,
                AboutActivity::class.java
            )
        },
        MenuItem("Profile", "profile screen", Icons.Default.Person) {
            navigateTo(
                this,
                ProfileActivity::class.java
            )
        },
        MenuItem("My Channels", "my channels screen", Icons.AutoMirrored.Filled.List) {
            navigateTo(this,
                ChannelsListActivity::class.java)
            finish()
        },

        MenuItem("Search channel", "search channel screen", Icons.Default.Search) {
            navigateTo(
                this,
                SearchChannelsActivity::class.java
            )
            finish()
        },
        MenuItem("Create channel", "create channel screen", Icons.Default.Add) {
            navigateTo(
                this,
                CreateChannelActivity::class.java
            )
            finish()
        },
        MenuItem("My Channel Invitations", "my channel invitations screen", Icons.Default.Notifications) {
            navigateTo(
                this,
                InvitationListActivity::class.java
            )
            finish()
        },
        MenuItem("Logout", "logout screen", Icons.AutoMirrored.Filled.ExitToApp) {
            WorkManager.getInstance(applicationContext).cancelAllWork()
            viewModel.logout()
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenuScreen(
                viewModel = viewModel,
                menuItems = menuItems,
                onNavigateBack = {
                    navigateTo(this, ChannelsListActivity::class.java)
                    finish()
                },
                onLogout = {
                    WorkManager.getInstance(applicationContext).cancelAllWork()
                    finishAffinity()
                    navigateTo(this, HomeActivity::class.java)
                }
            )
        }
    }
}