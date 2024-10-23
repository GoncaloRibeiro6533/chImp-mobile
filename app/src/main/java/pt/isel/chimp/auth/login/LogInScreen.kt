package pt.isel.chimp.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pt.isel.chimp.ui.theme.ChImpTheme

const val LOGIN_SCREEN_TEST_TAG = "LoginScreenTestTag"


@Composable
fun LoginScreen(
    //onNavigateBack: () -> Unit = {  },
){
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(REGISTER_SCREEN_TEST_TAG),
            /*
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
            },

             */
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(text ="Log In to your Account", fontSize = 30.sp, fontWeight = FontWeight.Bold)

                SimpleTextField("Enter Username", "")
                SimpleTextField("Enter Password", "")


            }

        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginView() {
    LoginScreen()
}