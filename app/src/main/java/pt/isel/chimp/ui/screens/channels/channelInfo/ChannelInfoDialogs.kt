package pt.isel.chimp.ui.screens.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

const val DIALOG_BUTTON = "dialog_button"
const val DIALOG_TEXT_FIELD = "dialog_text_field"
const val CONFIRM_BUTTON = "confirm_button"
const val CANCEL_BUTTON = "cancel_button"
const val DROPDOWN_BUTTON = "dropdown_button"


@Composable
fun ChannelDialog(
    buttonName: String,
    message: String,
    confirmMessage: String,
    placeHolderText: String,
    buttonColor: Color,
    buttonTextColor: Color,
    onConfirm: (String) -> Unit,
    modifier: Modifier,

    ) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf("Select Role") }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Text(text = message)
            },
            text = {
                Column {

                    if(buttonName != "Leave Channel") {
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = { Text(placeHolderText) },
                            modifier = Modifier.testTag(DIALOG_TEXT_FIELD)
                        )


                        if (buttonName == "Invite Member +") {
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
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Read Only") },
                                        onClick = {
                                            selectedOption = "Read Only"
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Read Write") },
                                        onClick = {
                                            selectedOption = "Read Write"
                                            expanded = false
                                        }
                                    )

                                }
                            }
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
                                openDialog = false
                            },
                            colors = ButtonColors(Color.LightGray, Color.Red, Color.Green, Color.Green),
                            modifier = Modifier.testTag(CANCEL_BUTTON)

                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                onConfirm(text)
                                openDialog = false
                            },
                            colors = ButtonColors(Color(0xFF32cd32), Color.Black, Color.Green, Color.Green),
                            modifier = Modifier.testTag(CONFIRM_BUTTON)

                        ) {
                            Text(confirmMessage)
                        }

                    }
                }
            }
        )
    }

    // Button to trigger dialog

    Button(

        onClick = { openDialog = true },
        colors = ButtonColors(buttonColor, buttonTextColor, Color.Green, Color.Green),
        modifier = modifier.testTag(DIALOG_BUTTON)
    ) {
        Text(buttonName)
    }



}


@Preview
@Composable
fun PreviewTextFieldDialogExample() {

    ChannelDialog(
        "Edit Channel Name", "Enter new channel name:", "OK", "ChannelName",
        Color(0xFF32cd32),
        Color.Black,
        {},
        Modifier
    )
}


@Preview
@Composable
fun PreviewTextFieldDialogExample2() {

    ChannelDialog(
        "Invite Member +", "Enter Member Username:", "Invite", "username",
        Color(0xFF32cd32),
        Color.Black,
        { },
        Modifier
    )
}

