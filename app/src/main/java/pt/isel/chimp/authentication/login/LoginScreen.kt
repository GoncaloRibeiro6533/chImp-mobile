package pt.isel.chimp.authentication.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.MockUserService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle



const val LOGIN_SCREEN_TEST_TAG = "LoginScreenTestTag"

private fun extractErrorMessage(input: String): String {
    val regex = """("title":\s*"([^"]+)")""".toRegex()
    val matchResult = regex.find(input)

    return matchResult?.groups?.get(2)?.value ?: "Unknown Error"
}

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
                horizontalAlignment = Alignment.CenterHorizontally,
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
                        //Text(text = "Login successful", style = TextStyle(fontSize = 24.sp))
                        onLoginSuccessful(currentState.user)
                    }
                    is LoginScreenState.Error -> {
                        ErrorAlert(
                            title = "Login Error",
                            message = currentState.exception.message ?: "",
                            buttonText = "Ok",
                            onDismiss = { viewModel.setIdleState() }
                        )
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        viewModel = LoginScreenViewModel(MockUserService(RepoMockImpl())),
        onLoginSuccessful = { },
        onBackRequested = { },
    )
}