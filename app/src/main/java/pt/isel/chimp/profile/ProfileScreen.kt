package pt.isel.chimp.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.about.ABOUT_SCREEN_TEST_TAG
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.infrastructure.CookiesRepo
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.service.mock.ChImpServiceMock
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme



@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel,
    onNavigateBack: () -> Unit
) {
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(ABOUT_SCREEN_TEST_TAG),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when (val currentState = viewModel.state.collectAsState().value) {
                    is ProfileScreenState.Idle -> {
                            viewModel.fetchProfile()
                    }
                    is ProfileScreenState.Loading -> {
                        LoadingView()
                    }
                    is ProfileScreenState.Success -> {
                        ProfileView(
                            state = currentState,
                            onEditUsernameClick = { viewModel.setEditState(currentState.profile) },
                        )
                    }
                    is ProfileScreenState.EditingUsername -> {
                        EditingUsernameView(
                            state = currentState,
                            onSaveIntent = { newUsername ->
                                viewModel.editUsername(newUsername)
                            },
                            onCancelIntent = { viewModel.setSuccessState(currentState.profile) }
                        )
                    }

                    is ProfileScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = currentState.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.fetchProfile() }

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
fun ProfileScreenPreview() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    ProfileScreen(viewModel = ProfileScreenViewModel(
        UserInfoRepo(preferences),
        ChImpServiceMock(CookiesRepo(preferences)).userService,
    ), onNavigateBack = {})
}
