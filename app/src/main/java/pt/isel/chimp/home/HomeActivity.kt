package pt.isel.chimp.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import pt.isel.chimp.CoroutineSseWorkItem
import pt.isel.chimp.about.AboutActivity
import pt.isel.chimp.authentication.login.LoginActivity
import pt.isel.chimp.authentication.register.RegisterActivity
import pt.isel.chimp.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.navigateTo

const val TAG = "CHIMP"

class HomeActivity : ComponentActivity() {


    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }

    private val viewModel by viewModels<HomeScreenViewModel>(
        factoryProducer = {
            HomeScreenViewModelFactory(userInfoRepository)
        }
    )


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
                        onRegisterRequested = { navigateTo(this, RegisterActivity::class.java) },
                        onLoggedIntent = {
                            navigateTo(this, ChannelsListActivity::class.java)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteDatabase("chimp-db")
        deleteSharedPreferences("preferences")
    }
}

