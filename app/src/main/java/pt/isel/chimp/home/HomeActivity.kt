package pt.isel.chimp.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.menu.MenuActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pt.isel.chimp.about.AboutActivity

import pt.isel.chimp.authentication.register.RegisterActivity
import pt.isel.chimp.authentication.login.LoginActivity
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.navigateTo

const val TAG = "CHIMP"

class HomeActivity : ComponentActivity() {

    private val viewModel by viewModels<HomeScreenViewModel>(
        factoryProducer = {
            HomeScreenViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    private val navigateToMenu: Intent by lazy {
        Intent(this, MenuActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChImpTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        viewModel = viewModel,
                        onAboutRequested = { navigateTo(this, AboutActivity::class.java) },
                        onLoginRequested = { navigateTo(this, LoginActivity::class.java) },
                        onSigninRequested = { navigateTo(this, RegisterActivity::class.java) },
                        onMenuRequested = { startActivity(navigateToMenu)}
                        )
                }
            }
        }
    }
}

