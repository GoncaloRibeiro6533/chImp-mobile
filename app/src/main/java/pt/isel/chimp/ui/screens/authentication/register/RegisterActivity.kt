package pt.isel.chimp.ui.screens.authentication.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.ui.screens.authentication.login.LoginActivity
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.navigateTo

class RegisterActivity: ComponentActivity() {

    private val viewModel by viewModels<RegisterScreenViewModel>(
        factoryProducer = {
            RegisterScreenViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChImpTheme {
                RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccessful = {
                        finish()
                        navigateTo(this, LoginActivity::class.java) },
                    onNavigateBack = { finish() }
                )
            }
        }

    }
}