package pt.isel.chimp.menu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import pt.isel.chimp.about.AboutActivity
import pt.isel.chimp.profile.ProfileActivity

class MenuActivity : ComponentActivity() {

    private val navigateToAboutIntent: Intent by lazy {
        Intent(this, AboutActivity::class.java)
    }
    private val navigateToProfileIntent: Intent by lazy {
        Intent(this, ProfileActivity::class.java)
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
        MenuItem("About", "about screen", Icons.Default.Info, { startActivity(navigateToAboutIntent) }),
        MenuItem("Profile", "profile screen", Icons.Default.Person, { startActivity(navigateToProfileIntent) }),
        MenuItem("Search channel", "search channel screen", Icons.Default.Search, { }),
        MenuItem("Create channel", "create channel screen", Icons.Default.Add, { }),
        MenuItem("Create registration invitation", "create channel invitation screen", Icons.Default.Add, { }),
        MenuItem("Create registration invitation", "create channel invitation screen", Icons.Default.Add, { }),
        MenuItem("Logout", "logout screen", Icons.AutoMirrored.Filled.ExitToApp, { }),
    )
}