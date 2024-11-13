package pt.isel.chimp.authentication.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.authentication.register.components.RegisterButton
import pt.isel.chimp.authentication.register.components.RegisterTextFields
import pt.isel.chimp.authentication.validateEmail
import pt.isel.chimp.authentication.validatePassword
import pt.isel.chimp.authentication.validateUsername

@Composable
fun RegisterView(
    onSubmit : (String, String, String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val invalidFields = (email.isEmpty() || username.isEmpty() || password.isEmpty()) ||
            email.isNotEmpty() && !validateEmail(email) ||
            username.isNotEmpty() && !validateUsername(username) ||
            password.isNotEmpty() && !validatePassword(password)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) { Text(
            text = "Register",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        ) }
        RegisterTextFields(
            email = email,
            username = username,
            password = password,
            onEmailChangeCallback = { email = it.trim() },
            onUsernameChangeCallback = { username = it.trim() },
            onPasswordChangeCallback = { password = it.trim() }
        )

        RegisterButton(enabled = !invalidFields) {
           onSubmit(username, password, email)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    RegisterView { _, _, _ -> }
}