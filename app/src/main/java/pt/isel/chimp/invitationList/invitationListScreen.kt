package pt.isel.chimp.invitationList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun InvitationScreen(
    viewModel: InvitationListViewModel,
    onNavigationBack: () -> Unit,
    onAccept: (ChannelParcelable) -> Unit = { },
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
                    is InvitationListScreenState.Uninitialized -> {
                        //Nothing to do
                    }
                    is InvitationListScreenState.Loading -> LoadingView()
                    is InvitationListScreenState.Success -> {
                        InvitationListView(
                            invitations = state.invitations,
                            onAccept = { invitation -> viewModel.acceptInvitation(invitation) },
                            onDecline = { id -> viewModel.declineInvitation(id) }
                        )
                    }
                    is InvitationListScreenState.SuccessOnAccept ->
                        onAccept(state.channelRole.first.toParcelable(state.channelRole.second))
                    is InvitationListScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { onNavigationBack() }
                        )
                    }
                }
            }

        }
    }
}