package pt.isel.chimp.authentication.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.authentication.register.RegisterActivity
import pt.isel.chimp.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo

class LoginActivity: ComponentActivity() {
    private val viewModel by viewModels<LoginScreenViewModel>(
        factoryProducer = {
            LoginScreenViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccessful = {
                    run {
                        navigateTo(this,MenuActivity::class.java)
                    }
                },

                onBackRequested = { finish() },
                onRegisterRequested = { navigateTo(this, RegisterActivity::class.java) },
            )
        }

    }
}