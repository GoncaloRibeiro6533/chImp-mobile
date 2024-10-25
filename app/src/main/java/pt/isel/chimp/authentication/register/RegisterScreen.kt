package pt.isel.chimp.authentication.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.authentication.register.components.RegisterButton
import pt.isel.chimp.authentication.register.components.RegisterTextFields
import pt.isel.chimp.authentication.validateEmail
import pt.isel.chimp.authentication.validatePassword
import pt.isel.chimp.authentication.validateUsername
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

const val REGISTER_SCREEN_TEST_TAG = "RegisterScreenTestTag"

private fun extractErrorMessage(input: String): String {
    val regex = """("title":\s*"([^"]+)")""".toRegex()
    val matchResult = regex.find(input)

    return matchResult?.groups?.get(2)?.value ?: "Unknown Error"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onRegisterSuccessful: () -> Unit,
    onNavigateBack: () -> Unit = {  },
){
    /*
    LaunchedEffect(state) {
        if (state is Loaded && state.value.isSuccess)
            onRegisterSuccessful()
    }
     */

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val invalidFields = (email.isEmpty() || username.isEmpty() || password.isEmpty()) ||
            email.isNotEmpty() && !validateEmail(email) ||
            username.isNotEmpty() && !validateUsername(username) ||
            password.isNotEmpty() && !validatePassword(password)

    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(REGISTER_SCREEN_TEST_TAG),

            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Register",
                    modifier = Modifier.padding(innerPadding)
                )
                RegisterTextFields(
                    email = email,
                    username = username,
                    password = password,
                    onEmailChangeCallback = { email = it },
                    onUsernameChangeCallback = { username = it },
                    onPasswordChangeCallback = { password = it }
                )

                RegisterButton(/*enabled = state !is Loading*/) {
                    if (invalidFields)
                        return@RegisterButton

                    onRegister(username, password, email)
                }
/*
                if (state is Loaded && state.value.isSuccess) {
                    Text(text = "Login successful", style = TextStyle(fontSize = 24.sp))
                }

                if (state is Loaded && state.value.isFailure) {
                    Text(text = "Register failed. Please try again", style = TextStyle(fontSize = 24.sp))
                    Spacer(modifier = Modifier.padding(6.dp))
                    val errorMsg = extractErrorMessage(state.value.toString())
                    Text(text = errorMsg, style = TextStyle(fontSize = 24.sp))
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }


        }
    }

 */

            }

        }
    }

}

@Composable
fun SimpleTextField(label: String, example: String) {
    var text by remember { mutableStateOf(TextFieldValue(example)) }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = { Text(label) },
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterView() {
    RegisterScreen(
        onRegister = { _, _, _ -> },
        onRegisterSuccessful = { },
        onNavigateBack = { }
    )
}