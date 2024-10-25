package pt.isel.chimp.profile

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.ui.theme.ChImpTheme


@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel,
    onNavigateBack: () -> Unit
) {
    val token = "token1"
    ChImpTheme {
        Log.d("ProfileScreen", "ProfileScreen recomposition")
        val currentState =  viewModel.state
        when (currentState) {
            is ProfileScreenState.Idle -> {
                LaunchedEffect(currentState) {
                    viewModel.fetchProfile(token)
                }
            }
            is ProfileScreenState.Loading -> {
                LoadingView()
            }
            is ProfileScreenState.Success -> {
                ProfileView(
                    profile = currentState.profile,
                    onEditUsernameClick = { viewModel.setEditState(currentState.profile) },
                )
            }
            is ProfileScreenState.EditingUsername -> {
                EditingUsernameView(
                    username = currentState.profile.username,
                    onSaveIntent = { newUsername ->
                       viewModel.editUsername(newUsername, token)
                    },
                    onCancelIntent = { viewModel.setSuccessState(currentState.profile) }
                )
            }
            is ProfileScreenState.Error -> {
                ErrorAlert(
                    title = "Error",
                    message = currentState.exception.message ?: "An error occurred",
                    buttonText = "Ok",
                    onDismiss = { viewModel.fetchProfile(token) }

                )
            }
        }

    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(viewModel = ProfileScreenViewModel(ChImpService()), {})
}
