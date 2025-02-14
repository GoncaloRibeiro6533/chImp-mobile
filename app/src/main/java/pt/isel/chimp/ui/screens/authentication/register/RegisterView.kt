package pt.isel.chimp.ui.screens.authentication.register

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.ui.screens.authentication.register.components.RegisterButton
import pt.isel.chimp.ui.screens.authentication.register.components.RegisterTextFields
import pt.isel.chimp.ui.screens.authentication.validateEmail
import pt.isel.chimp.ui.screens.authentication.validatePassword
import pt.isel.chimp.ui.screens.authentication.validateUsername

const val REGISTER_VIEW = "register_views"
const val REGISTER_TEXT_FIELDS = "register_text_fields"
const val REGISTER_BUTTON = "register_button"
@Composable
fun RegisterView(
    onSubmit: (String, String, String) -> Unit,
) {
    val orientation = LocalConfiguration.current.orientation
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val invalidFields = email.isEmpty() || username.isEmpty() || password.isEmpty() ||
            (email.isNotEmpty() && !validateEmail(email)) ||
            (username.isNotEmpty() && !validateUsername(username)) ||
            (password.isNotEmpty() && !validatePassword(password))

    if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
        // Layout para portrait
        PortraitRegisterLayout(
            email = email,
            username = username,
            password = password,
            onEmailChange = { email = it.trim() },
            onUsernameChange = { username = it.trim() },
            onPasswordChange = { password = it.trim() },
            invalidFields = invalidFields,
            onSubmit = onSubmit
        )
    } else {
        // Layout para landscape
        LandscapeRegisterLayout(
            email = email,
            username = username,
            password = password,
            onEmailChange = { email = it.trim() },
            onUsernameChange = { username = it.trim() },
            onPasswordChange = { password = it.trim() },
            invalidFields = invalidFields,
            onSubmit = onSubmit
        )
    }
}

@Composable
fun PortraitRegisterLayout(
    email: String,
    username: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    invalidFields: Boolean,
    onSubmit: (String, String, String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .testTag(REGISTER_VIEW)
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        RegisterTextFields(
            email = email,
            username = username,
            password = password,
            onEmailChangeCallback = onEmailChange,
            onUsernameChangeCallback = onUsernameChange,
            onPasswordChangeCallback = onPasswordChange,
            modifier = Modifier.testTag(REGISTER_TEXT_FIELDS)
        )

        RegisterButton(
            enabled = !invalidFields,
            modifier = Modifier.testTag(REGISTER_BUTTON),
        ) {
            onSubmit(username, password, email)
        }
    }
}

@Composable
fun LandscapeRegisterLayout(
    email: String,
    username: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    invalidFields: Boolean,
    onSubmit: (String, String, String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .testTag(REGISTER_VIEW),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            RegisterTextFields(
                email = email,
                username = username,
                password = password,
                onEmailChangeCallback = onEmailChange,
                onUsernameChangeCallback = onUsernameChange,
                onPasswordChangeCallback = onPasswordChange,
                modifier = Modifier.testTag(REGISTER_TEXT_FIELDS)
            )
        }

        RegisterButton(
            enabled = !invalidFields,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(0.5f)
                .testTag(REGISTER_BUTTON),
        ) {
            onSubmit(username, password, email)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    RegisterView { _, _, _ -> }
}