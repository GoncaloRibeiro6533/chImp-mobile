package pt.isel.chimp.auth.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

const val REGISTER_SCREEN_TEST_TAG = "RegisterScreenTestTag"


@Composable
fun RegisterScreen(
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
                Text(text ="Create your Account", fontSize = 30.sp, fontWeight = FontWeight.Bold)

                SimpleTextField("Create Username", "ex: Johnny1234")
                SimpleTextField("Create Password", "ex: 12@!rThoK09?r")
                SimpleTextField("Confirm Password", "")


            }

        }
    }

}

@Composable
fun SimpleTextField(label: String, example: String) {
    var text by remember { mutableStateOf(TextFieldValue(example)) }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = { Text(label) },
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterView() {
    RegisterScreen()
}