package pt.isel.chimp.menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import pt.isel.chimp.about.AboutActivity
import pt.isel.chimp.authentication.login.LoginActivity
import pt.isel.chimp.channels.createChannel.CreateChannelActivity
import pt.isel.chimp.profile.ProfileActivity
import pt.isel.chimp.utils.navigateTo

class MenuActivity : ComponentActivity() {

    private val logOutIntent : Intent by lazy {
        Intent(this, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenuScreen(
                menuItems = menuItems,
                onNavigateBack = { finish() }
            )
        }
    }

    private val menuItems = listOf(
        MenuItem("About", "about screen", Icons.Default.Info, { navigateTo(this, AboutActivity::class.java) }),
        MenuItem("Profile", "profile screen", Icons.Default.Person, { navigateTo(this, ProfileActivity::class.java) }),
        MenuItem("Search channel", "search channel screen", Icons.Default.Search, { }),
        MenuItem("Create channel", "create channel screen", Icons.Default.Add, { navigateTo(this, CreateChannelActivity::class.java) }),
        MenuItem("Create Channel invitation", "create channel invitation screen", Icons.Default.Add, { }),
        MenuItem("Create Registration invitation", "create registration invitation screen", Icons.Default.Add, { }),
        MenuItem("Logout", "logout screen", Icons.AutoMirrored.Filled.ExitToApp, {
            //todo
            startActivity(logOutIntent)
        }),
    )
}