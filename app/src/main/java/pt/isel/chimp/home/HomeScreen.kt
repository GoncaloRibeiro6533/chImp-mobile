package pt.isel.chimp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onAboutRequested : () -> Unit,
    onLoginRequested : () -> Unit = { },
    onRegisterRequested : () -> Unit = { },
    onLoggedIntent : () -> Unit = { }
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (viewModel.state) {
                    HomeScreenState.Idle -> {
                        HomePopPup()
                        viewModel.getSession()
                    }
                    HomeScreenState.Logged -> {
                        onLoggedIntent()
                    }
                    HomeScreenState.NotLogged -> {
                        HomeView(
                            onLoginRequested = onLoginRequested,
                            onRegisterRequested = onRegisterRequested
                        )
                    }
                }
            }
        }
    }
}

/*
@Composable
fun HomeS() {
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("HomeScreenTestTag")
                .background(MaterialTheme.colorScheme.background)
        )
        { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                LoadingView()
            }
        }
    }
}
 *//*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
  //  HomeScreen(HomeScreenViewModel(MockUserService(RepoMockImpl())), onAboutRequested = {})
    // HomeS()
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreviewDark() {
   // HomeScreen(HomeScreenViewModel(MockUserService(RepoMockImpl())), onAboutRequested = {})
    ///   HomeS()
}*/

