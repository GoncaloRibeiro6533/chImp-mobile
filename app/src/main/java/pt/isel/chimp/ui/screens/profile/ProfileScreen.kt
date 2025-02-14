package pt.isel.chimp.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import pt.isel.chimp.ui.screens.about.ABOUT_SCREEN_TEST_TAG
import pt.isel.chimp.ui.screens.components.LoadingView
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
                    is ProfileScreenState.Idle,
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


