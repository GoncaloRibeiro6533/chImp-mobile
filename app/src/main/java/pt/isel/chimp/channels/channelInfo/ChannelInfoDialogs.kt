package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChannelDialog(
    buttonName: String,
    message: String,
    confirmMessage: String,
    placeHolderText: String,
    buttonColor: Color,
    onConfirm : () -> Unit,

) {
    var openDialog by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Text(text = message)
                    },
            text = {
                Column {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text(placeHolderText) }
                    )
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

                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                onConfirm
                                openDialog = false
                            },
                            colors = ButtonColors(Color(0xFF32cd32), Color.Black, Color.Green, Color.Green)

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
        colors = ButtonColors(buttonColor, Color.Black, Color.Green, Color.Green)
    ) {
        Text(buttonName)
    }
}


@Preview
@Composable
fun PreviewTextFieldDialogExample() {

    ChannelDialog("Edit Channel Name", "Enter new channel name:", "OK", "ChannelName", Color(0xFF32cd32)) {}
}
