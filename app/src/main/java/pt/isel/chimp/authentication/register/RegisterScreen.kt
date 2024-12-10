package pt.isel.chimp.authentication.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.infrastructure.CookiesRepo
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.MockUserService
import pt.isel.chimp.service.repo.RepoMockImpl
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
    viewModel: RegisterScreenViewModel,
    onRegisterSuccessful: () -> Unit,
    onNavigateBack: () -> Unit = { }
){
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(REGISTER_SCREEN_TEST_TAG),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            ) {
                when(val currentState = viewModel.state.collectAsState().value){
                    is RegisterScreenState.Idle -> {
                        RegisterView (
                            onSubmit = { email, username, password ->
                                viewModel.registerUser(email, username, password)
                            }
                        )
                    }
                    is RegisterScreenState.Loading -> {
                        LoadingView()
                    }
                    is RegisterScreenState.Success -> {
                        SuccessView(
                            message = "User registered successfully",
                            onButtonClick = { onRegisterSuccessful() }
                        )
                    }
                    is RegisterScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
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

@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterView() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    RegisterScreen(
        viewModel = RegisterScreenViewModel(MockUserService(RepoMockImpl(),  CookiesRepo(preferences))),
        onRegisterSuccessful = {}
    )
}