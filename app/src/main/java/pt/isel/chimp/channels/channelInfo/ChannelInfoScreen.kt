package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelInfoScreen(
    viewModel: ChannelInfoViewModel,
    channel: Channel,
    role: Role,
    onNavigationBack: (channelP: ChannelParcelable) -> Unit,
    onCreateInvitation: () -> Unit,
    onChannelLeave: () -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val channelF: Channel = viewModel.channelFlow.collectAsState().value
    ChImpTheme {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = {
                            onNavigationBack(channelF.toParcelable(role))
                        }
                    ),
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
                    is ChannelInfoScreenState.Idle -> {
                      // Do nothing
                    }
                    is ChannelInfoScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelInfoScreenState.Success -> {
                        ChannelInfoView(
                            channelInfo = state.channelInfo,
                            onUpdateChannel = { newName ->
                                viewModel.updateChannelName(channel, newName) {

                                }
                            },
                            onLeaveChannel = {
                                viewModel.leaveChannel(state.channelInfo.channel.value) {
                                onNavigationBack(channelF.toParcelable(role)) } },
                            onInviteMember = { onCreateInvitation() }
                        )
                    }
                    is ChannelInfoScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { state.onDismiss() }
                        )
                    }

                    is ChannelInfoScreenState.SuccessOnLeave -> {
                        onChannelLeave()
                    }
                }
            }
        }
    }
}
