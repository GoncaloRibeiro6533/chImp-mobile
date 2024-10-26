package pt.isel.chimp.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.menu.MenuActivity

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
            HomeScreen(
                viewModel = viewModel,
                onMenuRequested = { startActivity(navigateToMenu)}
            )
        }
    }
}

