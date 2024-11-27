package pt.isel.chimp.authentication.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.MockUserService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme


const val LOGIN_SCREEN_TEST_TAG = "LoginScreenTestTag"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel,
    onLoginSuccessful: (AuthenticatedUser) -> Unit,
    onBackRequested: () -> Unit,
    onRegisterRequested: () -> Unit = { }

){
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LOGIN_SCREEN_TEST_TAG),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            ) {
                when (val currentState = viewModel.state) {
                    is LoginScreenState.Idle ->
                        LoginView(
                            onSubmit = { username, password ->
                                viewModel.fetchLogin(username, password) },
                            onRegisterRequested = onRegisterRequested
                        )
                    is LoginScreenState.Loading -> {
                        LoadingView()
                    }
                    is LoginScreenState.Success -> {
                        onLoginSuccessful(currentState.user)
                    }
                    is LoginScreenState.Error -> {
                        ErrorAlert(
                            title = "Login Error",
                            message = currentState.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.setIdleState() }
                        )
                    }
                }
            }
        }
    }
}




@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    LoginScreen(
        viewModel = LoginScreenViewModel(
            repo = UserInfoRepo(preferences),
            userService = MockUserService(RepoMockImpl())
        ),
        onLoginSuccessful = { },
        onBackRequested = { },
    )
}