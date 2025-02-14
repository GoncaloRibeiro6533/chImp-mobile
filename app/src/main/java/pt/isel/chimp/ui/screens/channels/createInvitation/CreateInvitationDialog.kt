package pt.isel.chimp.ui.screens.channels.createInvitation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import pt.isel.chimp.ui.screens.channels.channelInfo.CANCEL_BUTTON
import pt.isel.chimp.ui.screens.channels.channelInfo.CONFIRM_BUTTON
import pt.isel.chimp.ui.screens.channels.channelInfo.DIALOG_TEXT_FIELD
import pt.isel.chimp.ui.screens.channels.channelInfo.DROPDOWN_BUTTON
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.user.User


@Composable
fun CreateInvitationDialog(
    message: String,
    confirmMessage: String,
    placeHolderText: String,
    buttonColor: Color,
    buttonTextColor: Color,
    onSearch : (String) -> Unit,
    onInvite: (User, Role) -> Unit,
    onCancel: () -> Unit,
    users: StateFlow<List<User>>,
    isFetching: Boolean = false
) {

    val users = users.collectAsState().value
    var selectedOption by rememberSaveable { mutableStateOf("Select Role") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedUser: User? by rememberSaveable { mutableStateOf(null) }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = {
            Text(text = message)
        },
        text = {
            Column {
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                        selectedUser = null
                        if (it.text.isNotBlank()) onSearch(it.text)
                    },
                    placeholder = { Text(placeHolderText) },
                    modifier = Modifier.testTag(DIALOG_TEXT_FIELD),
                )

                if (isFetching) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (users.isNotEmpty() && selectedUser == null && text.text.isNotBlank()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .border(1.dp, Color.Gray)
                    ) {
                        items(users) { user ->
                            Text(
                                text = user.username,
                                modifier = Modifier
                                    .fillMaxWidth().padding(4.dp)
                                    .clickable {
                                        selectedUser = user
                                        text = TextFieldValue(user.username)
                                    }
                                    .padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonColors(Color(0xFF1e90ff), Color.White, Color.Green, Color.Green),
                        modifier = Modifier.testTag(DROPDOWN_BUTTON)
                    ) {
                        Text(selectedOption)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Read Only") },
                            onClick = {
                                selectedOption = Role.READ_ONLY.value
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Read Write") },
                            onClick = {
                                selectedOption = Role.READ_WRITE.value
                                expanded = false
                            }
                        )

                    }
                }
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterHorizontally)
                ) {
                    Button(

                        onClick = {
                            onCancel()
                        },
                        colors = ButtonColors(Color.Red, Color.LightGray, Color.Green, Color.Green),
                        modifier = Modifier.testTag(CANCEL_BUTTON)

                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val user = selectedUser
                            if (user != null && selectedOption != "Select Role") {
                                onInvite(user, Role.entries.first { it.value == selectedOption })
                            }
                        },
                        colors = ButtonColors(Color(0xFF32cd32), Color.Black, Color.Gray, Color.Black),
                        modifier = Modifier.testTag(CONFIRM_BUTTON),
                        enabled = selectedUser != null && selectedOption != "Select Role"

                    ) {
                        Text(confirmMessage)
                    }

                }
            }
        }
    )
}





