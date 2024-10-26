package pt.isel.chimp.channels.createChannel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.DropdownMenuCustom

@Composable
fun CreateChannelView() {
    var channelName by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf("PUBLIC") }
    val visibilityOptions = listOf("PUBLIC", "PRIVATE")
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Channel",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Channel Name",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = channelName,
            onValueChange = { channelName = it },
            label = { Text("Channel Name") },
            //modifier = Modifier.fillMaxWidth(0.8f)
        )

        DropdownMenuCustom(
            options = visibilityOptions,
            selectedValue = visibility,
            onValueChange = { visibility = it },
            label = "Visibility"
        )

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Create Channel")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateChannelViewPreview() {
    ChImpTheme {
        CreateChannelView()
    }
}