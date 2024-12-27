package pt.isel.chimp.invitationList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import pt.isel.chimp.channels.channelInfo.ChannelInfoScreenState
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun InvitationScreen(
    viewModel: InvitationListViewModel,
    onNavigationBack: () -> Unit,
    onAccept: () -> Unit,
    onDecline: (Int) -> Unit
) {
    ChImpTheme {
        val state =viewModel.state.collectAsState().value
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    NavigationHandlers(onBackRequested = onNavigationBack),
            content = {Text(text = "")}
                )
            }
        ) { innerPadding ->

            Column(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(innerPadding)
            ) {

                when (state) {
                    is InvitationListScreenState.Idle -> {
                        viewModel.getInvitations()
                    }
                    is InvitationListScreenState.Loading -> {
                        LoadingView()

                    }
                    is InvitationListScreenState.Success -> {

                        InvitationListView(
                            invitationsList = state.invitations,
                            onAccept = {id -> viewModel.acceptInvitation(id) },
                            onDecline = { id -> viewModel.declineInvitation(id) }
                        )
                    }

                    is InvitationListScreenState.SuccessOnAccept -> {
                        onAccept
                    }

                    is InvitationListScreenState.SuccessOnDecline -> {
                        onDecline
                    }

                    is InvitationListScreenState.Error -> {

                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = {  }
                        )
                    }

                }


            }

        }
    }
}