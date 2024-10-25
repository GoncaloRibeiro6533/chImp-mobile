package pt.isel.chimp.authentication.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.authentication.register.RegisterActivity
import pt.isel.chimp.domain.AuthenticatedUser
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.navigateTo

class LoginActivity: ComponentActivity() {
    private val viewModel by viewModels<LoginScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChImpTheme {
                LoginScreen(
                    //state = viewModel.login,
                    onLogin = { username, password ->
                        AuthenticatedUser(1, "token")
                        //viewModel.fetchLogin(app.mainService, username, password)
                    },
                    /* //todo navigate to profile
                    onLoginSuccessful = { userInfo ->
                        run {
                            app.userInfo = userInfo
                            //UserPreferencesActivity.navigateTo(activity)
                        }
                    },

                     */
                    onBackRequested = { finish() },
                    onRegisterRequested = { navigateTo(this, RegisterActivity::class.java) }
                )
            }

        }

    }
}