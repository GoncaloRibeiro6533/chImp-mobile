package pt.isel.chimp.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.infrastructure.CookiesRepo
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.ChImpServiceMock
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

data class MenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit

)


@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    menuItems: List<MenuItem>,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
    ) {
    val state = viewModel.state
    ChImpTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (state == MenuScreenState.Idle || state is MenuScreenState.Error) {
                    TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                HorizontalDivider()
            when (state) {
                is MenuScreenState.Idle -> {
                    LazyColumn {
                        items(menuItems) { item ->
                            MenuItemView(item)
                            HorizontalDivider()
                        }
                    }
                }
                is MenuScreenState.LoggingOut -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Texto de logout
                            Text(
                                text = "Exiting...",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            // Indicador de progresso
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                is MenuScreenState.LoggedOut -> {
                    onLogout()
                }
                is MenuScreenState.Error -> {
                    ErrorAlert(
                        title = "Error",
                        message = state.error.message,
                        buttonText = "Ok",
                        onDismiss = { onLogout() }
                    )
                }
            }
        }
        }
    }
}





@Composable
private fun MenuItemView(item: MenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            modifier = Modifier.width(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = item.title,
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Preview
@Composable
fun MenuItemViewPreview() {
    MenuItemView(MenuItem("About", "about screen", Icons.Default.Info, { }),)
}



@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    MenuScreen(
        viewModel = MenuViewModel(
            repo = UserInfoRepo(preferences),
            service = ChImpServiceMock(CookiesRepo(preferences))
        ),
        menuItems = listOf(
            MenuItem("About", "about screen", Icons.Default.Info, { }),
            MenuItem("Profile", "profile screen", Icons.Default.Person, { }),
            ),
        onNavigateBack ={},
        onLogout = {}
    )
}