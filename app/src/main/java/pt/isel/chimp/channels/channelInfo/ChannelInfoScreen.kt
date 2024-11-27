package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.channels.generalComponents.ChannelLogo
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.MockChannelService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelInfoScreen(
    viewModel: ChannelInfoViewModel,
    channel: Channel,
    onNavigationBack: () -> Unit,
    onChannelLeave: () -> Unit
) {

    val user = User(1, "Bob", "bob@example.com")

    val state = viewModel.state
    val token = "token1"
    ChImpTheme {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = onNavigationBack
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
                        viewModel.getChannelMembers(token, channel.id)
                    }
                    is ChannelInfoScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelInfoScreenState.Success -> {

                        ChannelInfoView(
                            token =token,
                            user = user,
                            channel = channel,
                            members = state.channelMembers,
                            viewModel = viewModel,
                            //innerPadding = innerPadding

                        )
                    }
                    is ChannelInfoScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = " ",
                            buttonText = "Ok",
                            onDismiss = { viewModel.getChannelMembers(token, channel.id) }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelScreenPreview() {
    ChannelInfoScreen(
        viewModel = ChannelInfoViewModel(MockChannelService(RepoMockImpl())),
        channel = Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onChannelLeave = { }
    )
}