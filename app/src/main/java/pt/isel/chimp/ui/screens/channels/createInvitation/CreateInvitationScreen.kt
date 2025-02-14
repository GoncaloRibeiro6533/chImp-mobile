package pt.isel.chimp.ui.screens.channels.createInvitation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pt.isel.chimp.ui.screens.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.ui.screens.profile.ErrorAlert
import pt.isel.chimp.ui.theme.ChImpTheme


@Composable
fun CreateInvitationScreen(
    viewModel: CreateInvitationViewModel,
    channel: Channel,
    onNavigationBack: () -> Unit,
) {
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize().background(Color.Transparent),
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            ) {
                when (val currentState = viewModel.state.collectAsState().value) {
                    is CreateInvitationScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = currentState.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.setIdle() }
                        )
                    }
                    is CreateInvitationScreenState.SearchUser,
                    CreateInvitationScreenState.Typing,
                    CreateInvitationScreenState.Idle -> {
                        CreateInvitationDialog (
                            "Enter Member Username:",
                            "Invite",
                            "username",
                            Color(0xFF32cd32),
                            Color.Black,
                            { username -> viewModel.searchUsers(username) },
                            { user, role -> viewModel.inviteMember(channel, user, role) },
                            { onNavigationBack() },
                            viewModel.users,
                            currentState is CreateInvitationScreenState.SearchUser
                        )
                    }
                    CreateInvitationScreenState.Success -> { onNavigationBack() }
                    is CreateInvitationScreenState.Submitting,
                    CreateInvitationScreenState.Loading -> LoadingView()
                }
            }
        }
    }

}