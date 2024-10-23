package pt.isel.chimp.authentication.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pt.isel.chimp.authentication.DEFAULT_LOGIN_RESPONSE
import pt.isel.chimp.authentication.login.components.LoginButton
import pt.isel.chimp.authentication.login.components.LoginTextFields
import pt.isel.chimp.authentication.register.REGISTER_SCREEN_TEST_TAG
import pt.isel.chimp.authentication.validatePassword
import pt.isel.chimp.authentication.validateUsername
import pt.isel.chimp.domain.AuthenticatedUser
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

const val LOGIN_SCREEN_TEST_TAG = "LoginScreenTestTag"

private fun extractErrorMessage(input: String): String {
    val regex = """("title":\s*"([^"]+)")""".toRegex()
    val matchResult = regex.find(input)

    return matchResult?.groups?.get(2)?.value ?: "Unknown Error"
}

var newUsername = ""

@Composable
fun LoginScreen(
    //state: LoadState<LoginResponse> = Idle,
    onLogin: (String, String) -> AuthenticatedUser = { _, _ -> DEFAULT_LOGIN_RESPONSE },
    //onLoginSuccessful: (UserInfo) -> Unit,
    onBackRequested: () -> Unit,
    onRegisterRequested: () -> Unit = { }
){

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val invalidFields = (username.isEmpty() || password.isEmpty()) ||
            username.isNotEmpty() && !validateUsername(username) ||
            password.isNotEmpty() && !validatePassword(password)

    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(REGISTER_SCREEN_TEST_TAG),
            topBar = { TopBar(NavigationHandlers(onBackRequested = onBackRequested)) },
            ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Login",
                    modifier = Modifier.padding(innerPadding)
                )

                LoginTextFields(
                    username = username,
                    password = password,
                    onUsernameChangeCallback = { newUsername = it; username = it },
                    onPasswordChangeCallback = { password = it }
                )

                LoginButton(/*enabled = state !is Loading*/) {
                    if (invalidFields)
                        return@LoginButton

                    onLogin(username, password)
                }
/*
                if (state is Loaded && state.value.isSuccess) {
                    newUsername = username
                    Text(text = "Login successful", style = TextStyle(fontSize = 24.sp))
                }

                if (state is Loaded && state.value.isFailure) {
                    Text(text = "Login failed. Please try again", style = TextStyle(fontSize = 24.sp))
                    Spacer(modifier = Modifier.padding(6.dp))
                    val errorMsg = extractErrorMessage(state.value.toString())
                    Text(text = errorMsg, style = TextStyle(fontSize = 24.sp))
                    Spacer(modifier = Modifier.padding(12.dp))
                }

 */
                val annotatedString = buildAnnotatedString {
                    withStyle(style = androidx.compose.ui.text.SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Sign Up")
                    }
                }
                Row {
                    Text(text = "Don't have an account? ", style = TextStyle(fontSize = 24.sp))
                    ClickableText(text = annotatedString, onClick = {onRegisterRequested()}, style = TextStyle(fontSize = 24.sp))
                }
            }

            }

        }
    }




@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginView() {
    LoginScreen(
        //state = Idle,
        onLogin = { _, _ -> DEFAULT_LOGIN_RESPONSE },
        //onLoginSuccessful = { },
        onBackRequested = { }
    )
}