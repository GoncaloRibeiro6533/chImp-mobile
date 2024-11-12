package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
    onNavigationBack: () -> Unit = { }
) {

    val state = viewModel.state
    val token = "token1"
    ChImpTheme {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = onNavigationBack
                    )
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
                        viewModel.getChannelMembers(token, channel)
                    }
                    is ChannelInfoScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelInfoScreenState.Success -> {
                        val members = state.channelMembers
                        LazyColumn(
                            contentPadding = PaddingValues(
                                top = 0.dp,
                                bottom = innerPadding.calculateBottomPadding(),
                                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(members) { member ->

                                MemberView(member.first, member.second.role)

                            }
                        }
                    }
                    is ChannelInfoScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.getChannelMembers(token, channel) }
                        )
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
        Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { }
    )
}