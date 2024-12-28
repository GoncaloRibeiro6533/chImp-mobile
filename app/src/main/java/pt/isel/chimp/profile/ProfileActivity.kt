package pt.isel.chimp.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo

class ProfileActivity : ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val chImpRepository by lazy { (application as DependenciesContainer).repo }

    private val viewModel by viewModels<ProfileScreenViewModel>(
        factoryProducer = {
            ProfileScreenViewModelFactory(
                userInfoRepository,
                chImpService,
                chImpRepository
            )
        }
    )

    private fun onNavigationBack() {
        navigateTo(
            this,
            MenuActivity::class.java
        )
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { onNavigationBack() },
            )
        }
    }
}