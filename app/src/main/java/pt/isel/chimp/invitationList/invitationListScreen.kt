package pt.isel.chimp.invitationList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.UserParcelable
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
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
                    is InvitationListScreenState.Idle -> {
                        viewModel.getInvitations()
                    }
                    is InvitationListScreenState.SavingData,
                    is InvitationListScreenState.Loading -> {
                        LoadingView()
                        if (state is InvitationListScreenState.SavingData)
                            viewModel.saveInvitations(state.invitations)
                    }
                    is InvitationListScreenState.Success -> {

                        InvitationListView(
                            invitations = state.invitations,
                            onAccept = {invitation -> viewModel.acceptInvitation(invitation) },
                            onDecline = { id -> viewModel.declineInvitation(id) }
                        )
                    }

                    is InvitationListScreenState.SuccessOnAccept -> {

                        val channel = state.channelRole.first
                        val role = state.channelRole.second
                        val creator = channel.creator

                        val userParcelable = UserParcelable(
                            creator.id,
                            creator.username,
                            creator.email
                        )

                        val parcelable = ChannelParcelable(
                            channel.id,
                            channel.name,
                            userParcelable,
                            channel.visibility,
                            role
                        )
                        onAccept(parcelable)
                    }

                    is InvitationListScreenState.Error -> {

                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.setUninitialized() }
                        )
                    }

                }


            }

        }
    }
}