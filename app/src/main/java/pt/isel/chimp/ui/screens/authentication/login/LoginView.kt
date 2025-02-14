package pt.isel.chimp.ui.screens.authentication.login

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.R
import pt.isel.chimp.ui.screens.authentication.login.components.LoginButton
import pt.isel.chimp.ui.screens.authentication.login.components.LoginTextFields
import pt.isel.chimp.ui.screens.authentication.validatePassword
import pt.isel.chimp.ui.screens.authentication.validateUsername

const val LOGIN_VIEW = "login_view"
const val LOGIN_TEXT_FIELDS = "login_text_fields"
const val LOGIN_BUTTON = "login_button"
const val REGISTER_ANCHOR = "register_anchor"

@Composable
fun LoginView(
    onSubmit: (String, String) -> Unit,
    onRegisterRequested: () -> Unit
){
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val invalidFields = (username.isEmpty() || password.isEmpty()) ||
            username.isNotEmpty() && !validateUsername(username) ||
            password.isNotEmpty() && !validatePassword(password)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp).fillMaxWidth().testTag(LOGIN_VIEW)
    ) {

        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.titleLarge
            )
        }
        LoginTextFields(
            username = username,
            password = password,
            onUsernameChangeCallback = { username = it },
            onPasswordChangeCallback = { password = it },
            modifier = Modifier.testTag(LOGIN_TEXT_FIELDS)
        )
        LoginButton(
            enabled = !invalidFields,
            modifier = Modifier.testTag(LOGIN_BUTTON),
            onLoginClickCallback = {
            onSubmit(username, password)
        })
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(stringResource(id = R.string.sign_up_annotated_string))
            }
        }
        Row (
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = stringResource(id = R.string.dont_have_account_text)
                , style = TextStyle(fontSize = 18.sp))
            Text(
                text = annotatedString,
                style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.clickable { onRegisterRequested() }.testTag(REGISTER_ANCHOR)
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