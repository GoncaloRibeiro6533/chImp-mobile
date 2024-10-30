package pt.isel.chimp.authentication.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.authentication.login.components.LoginButton
import pt.isel.chimp.authentication.login.components.LoginTextFields
import pt.isel.chimp.authentication.validatePassword
import pt.isel.chimp.authentication.validateUsername

@Composable
fun LoginView(
    onSubmit: (String, String) -> Unit,
    onRegisterRequested: () -> Unit
){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val invalidFields = (username.isEmpty() || password.isEmpty()) ||
            username.isNotEmpty() && !validateUsername(username) ||
            password.isNotEmpty() && !validatePassword(password)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(60.dp).fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleLarge
            )
        }
        LoginTextFields(
            username = username,
            password = password,
            onUsernameChangeCallback = { username = it },
            onPasswordChangeCallback = { password = it }
        )
        LoginButton(enabled = !invalidFields) {
            onSubmit(username, password)
        }
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("Sign Up")
            }
        }
        Row (
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
        ){
            Text(text = "Don't have an account? ", style = TextStyle(fontSize = 18.sp))
            ClickableText(
                text = annotatedString,
                onClick = {onRegisterRequested()},
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewLoginView() {
    LoginView(
        onSubmit = { _, _ -> },
        onRegisterRequested = { }
    )
}