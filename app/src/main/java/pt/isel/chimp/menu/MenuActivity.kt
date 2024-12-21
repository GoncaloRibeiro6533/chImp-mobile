package pt.isel.chimp.menu

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
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.about.AboutActivity
import pt.isel.chimp.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.channels.createChannel.CreateChannelActivity
import pt.isel.chimp.channels.searchChannels.SearchChannelsActivity
import pt.isel.chimp.profile.ProfileActivity
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
        },
        MenuItem("Create channel", "create channel screen", Icons.Default.Add) {
            navigateTo(
                this,
                CreateChannelActivity::class.java
            )
        },
        MenuItem("My Channel Invitations", "my channel invitations screen", Icons.Default.Notifications) {

        },
        MenuItem("Logout", "logout screen", Icons.AutoMirrored.Filled.ExitToApp) {
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
                    //navigateTo(this, HomeActivity::class.java)
                    //finishAffinity()
                    finish()
                }
            )
        }
    }
}