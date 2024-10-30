package pt.isel.chimp.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R
import pt.isel.chimp.service.MockUserService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onAboutRequested : () -> Unit,
    onLoginRequested : () -> Unit = { },
) {

    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("HomeScreenTestTag")
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onAboutRequested = onAboutRequested,
                    )
                )
            }
        ) { innerPadding ->
            Log.i(TAG, "ChIMP content: composing")
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Image(
                    modifier = Modifier.padding(16.dp),
                    painter = painterResource(id = R.drawable.ic_logo_app),
                    contentDescription = "ChIMP logo"
                )
                Text(
                    text ="Welcome to ChIMP",
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = onLoginRequested) {
                    Text(text = "Log In")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeScreenViewModel(MockUserService(RepoMockImpl())), onAboutRequested = {},
    )
}