package pt.isel.chimp.channels.searchChannels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.channels.channelsList.ChannelListView
import pt.isel.chimp.channels.channelsList.components.ChannelItem
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.infrastructure.CookiesRepo
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.MockChannelService
import pt.isel.chimp.service.repo.ChannelRepoMock.Companion.channels
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun SearchChannelsScreen(
    viewModel: SearchChannelsViewModel,
    onMenuRequested: () -> Unit = { },
    onChannelSelected: (Channel) -> Unit = { }
) {
    ChImpTheme {
        val state = viewModel.state.collectAsState().value
        val searchQuery = remember { mutableStateOf("") }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(onMenuRequested = onMenuRequested))
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
                        viewModel.getChannels(it, 10, 0)
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
                        //LoadingView()
                    }
                    is SearchChannelsScreenState.Success -> {
                        //ChannelListView(state.channels) { ch ->
                        //    onChannelSelected(ch)
                        //}
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



@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchChannelPreview() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    SearchChannelsScreen(
        viewModel = SearchChannelsViewModel(
            MockChannelService(RepoMockImpl(), CookiesRepo(preferences))
        ),
        onMenuRequested = { },
        onChannelSelected = { }
    )
}