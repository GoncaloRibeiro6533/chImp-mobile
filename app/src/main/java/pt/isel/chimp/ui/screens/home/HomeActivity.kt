package pt.isel.chimp.ui.screens.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.ui.screens.about.AboutActivity
import pt.isel.chimp.ui.screens.authentication.login.LoginActivity
import pt.isel.chimp.ui.screens.authentication.register.RegisterActivity
import pt.isel.chimp.ui.screens.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.navigateTo

const val TAG = "CHIMP"

class HomeActivity : ComponentActivity() {


    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }

    private val cookieRepo by lazy { (application as DependenciesContainer).cookieRep }
    private val viewModel by viewModels<HomeScreenViewModel>(
        factoryProducer = {
            HomeScreenViewModelFactory(userInfoRepository, cookieRepo)
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.getSession()
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
                            finish()
                            navigateTo(this, ChannelsListActivity::class.java)
                        }
                    )
                }
            }
        }
    }


}

