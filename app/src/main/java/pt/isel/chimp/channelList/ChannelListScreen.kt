package pt.isel.chimp.channelList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelListScreen(
    onMenuRequested: () -> Unit = { }

) {
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(onMenuRequested = onMenuRequested))
            },
        )
        { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(text ="Channels", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelListScreenPreview() {
    ChannelListScreen()
}
