package pt.isel.chimp.menu

import android.content.Intent
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
import pt.isel.chimp.about.AboutActivity
import pt.isel.chimp.channels.UserParcelable
import pt.isel.chimp.channels.channel.ChannelActivity
import pt.isel.chimp.channels.createChannel.CreateChannelActivity
import pt.isel.chimp.channels.searchChannels.SearchChannelsActivity
import pt.isel.chimp.home.HomeActivity
import pt.isel.chimp.profile.ProfileActivity
import pt.isel.chimp.utils.navigateTo

class MenuActivity : ComponentActivity() {


    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val chImpRepo by lazy { (application as DependenciesContainer).repo }
    private val db by lazy { (application as DependenciesContainer).clientDB }
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
            /*navigateTo(this,
                ChannelsListActivity::class.java)*/
            finish()
        },

        MenuItem("Search channel", "search channel screen", Icons.Default.Search) { user->
            finish()
            val parcelable = UserParcelable(user.id, user.username, user.email)
            val intent = Intent(this, SearchChannelsActivity::class.java)
                .putExtra("user", parcelable)
            finish()
            this.startActivity(intent)
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
            WorkManager.getInstance(applicationContext).cancelAllWork()
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.getUserInfo()
        setContent {
            MenuScreen(
                viewModel = viewModel,
                menuItems = menuItems,
                onNavigateBack = {
                    //navigateTo(this, ChannelsListActivity::class.java)
                    finish()
                },
                onLogout = {
                    //
                    //finishAffinity()
                    //finish previous activity
                    WorkManager.getInstance(applicationContext).cancelAllWork()
                    finishAffinity()
                    navigateTo(this, HomeActivity::class.java)
                }
            )
        }
    }
}