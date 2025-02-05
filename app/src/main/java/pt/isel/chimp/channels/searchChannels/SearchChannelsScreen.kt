package pt.isel.chimp.channels.searchChannels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.domain.UserParcelable
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun SearchChannelsScreen(
    viewModel: SearchChannelsViewModel,
    onBackRequest: () -> Unit = { },
    onChannelSelected: (ChannelParcelable) -> Unit = { },
) {
    ChImpTheme {
        val state = viewModel.state.collectAsState().value
        val searchQuery = remember { mutableStateOf("") }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onBackRequest))
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                    Text(
                        text = "Search Channels",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    OutlinedTextField(
                        value = searchQuery.value,
                        onValueChange = {
                            searchQuery.value = it
                            viewModel.getChannels(it,  20, 0)
                        },
                        label = { Text("Search") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                when (state) {
                    is SearchChannelsScreenState.Loading -> {
                        LoadingView()
                    }
                    is SearchChannelsScreenState.Typing -> {
                        //Does nothing
                    }
                    is SearchChannelsScreenState.Success -> {
                        SearchChannelListView(state.channels, state.channelsOfUser) {
                            viewModel.addUserToChannel(it.toChannel())
                        }
                    }
                    is SearchChannelsScreenState.EnteringChannel -> {
                        LoadingView()
                         onChannelSelected(
                            state.channel.toParcelable(state.role)
                         )
                    }
                    is SearchChannelsScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok"
                        ) { }
                    }
                }
            }
        }
    }
}