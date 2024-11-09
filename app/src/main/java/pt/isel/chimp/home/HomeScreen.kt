package pt.isel.chimp.home

import android.content.res.Configuration
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.R
import pt.isel.chimp.service.mock.MockUserService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onAboutRequested : () -> Unit,
    onLoginRequested : () -> Unit = { },
    onRegisterRequested : () -> Unit = { },
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
                    .padding(innerPadding),
            ) {
                Image(
                    modifier = Modifier.padding(10.dp).size(250.dp),
                    painter = painterResource(id = R.drawable.ic_logo_app4),
                    contentDescription = "ChIMP logo",
                )
                Text(
                    text = "Welcome to ChIMP",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(bottom = 15.dp),
                    fontSize = 30.sp
                )
                Button(
                    onClick = onLoginRequested,
                    modifier = Modifier.width(150.dp).padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5B84E),
                        contentColor = Color(0xff181d26)
                    )
                ) {
                    Text(text = "Log In")
                }
                Button(
                    onClick = onRegisterRequested,
                    modifier = Modifier.width(150.dp).padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5B84E),
                        contentColor = Color(0xff181d26)
                    )
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeScreenViewModel(MockUserService(RepoMockImpl())), onAboutRequested = {})
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreviewDark() {
    HomeScreen(HomeScreenViewModel(MockUserService(RepoMockImpl())), onAboutRequested = {})
}
