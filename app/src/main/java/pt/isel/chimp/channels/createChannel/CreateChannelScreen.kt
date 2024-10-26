package pt.isel.chimp.channels.createChannel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.service.MockChannelService
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun CreateChannelScreen(
    viewModel: CreateChannelViewModel,
    onNavigateBack: () -> Unit = { },
    onChannelCreated: () -> Unit)
{
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //TODO add when
                CreateChannelView()

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateChannelScreenPreview() {
    CreateChannelScreen(
        viewModel = CreateChannelViewModel(MockChannelService()),
        onNavigateBack = { },
        onChannelCreated = { }
    )
}