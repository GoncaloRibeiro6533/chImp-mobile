package pt.isel.chimp.ui.screens.channels.createChannel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.DropdownMenuCustom

@Composable
fun CreateChannelView(
    onSubmit: (String, String) -> Unit
) {
    val orientation = LocalConfiguration.current.orientation
    var channelName by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf("PUBLIC") }
    val visibilityOptions = listOf("PUBLIC", "PRIVATE")

    if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
        CreateChannelPortraitLayout(
            channelName = channelName,
            onChannelNameChange = { channelName = it },
            visibility = visibility,
            onVisibilityChange = { visibility = it },
            onSubmit = { onSubmit(channelName, visibility) },
            visibilityOptions = visibilityOptions
        )
    } else {
        CreateChannelLandscapeLayout(
            channelName = channelName,
            onChannelNameChange = { channelName = it },
            visibility = visibility,
            onVisibilityChange = { visibility = it },
            onSubmit = { onSubmit(channelName, visibility) },
            visibilityOptions = visibilityOptions
        )
    }
}

@Composable
fun CreateChannelPortraitLayout(
    channelName: String,
    onChannelNameChange: (String) -> Unit,
    visibility: String,
    onVisibilityChange: (String) -> Unit,
    onSubmit: () -> Unit,
    visibilityOptions: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
    ) {
        Text(
            text = "Create Channel",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 1.dp)
        )

        Text(
            text = "Channel Name",
            fontSize = 10.sp,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 1.dp)
        )

        TextField(
            value = channelName,
            onValueChange = onChannelNameChange,
            label = { Text("Channel Name") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        DropdownMenuCustom(
            options = visibilityOptions,
            selectedValue = visibility,
            onValueChange = onVisibilityChange,
            label = "Visibility"
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Create Channel")
        }
    }
}

@Composable
fun CreateChannelLandscapeLayout(
    channelName: String,
    onChannelNameChange: (String) -> Unit,
    visibility: String,
    onVisibilityChange: (String) -> Unit,
    onSubmit: () -> Unit,
    visibilityOptions: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Create Channel",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Text(
                text = "Channel Name",
                fontSize = 20.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = channelName,
                onValueChange = onChannelNameChange,
                label = { Text("Channel Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            DropdownMenuCustom(
                options = visibilityOptions,
                selectedValue = visibility,
                onValueChange = onVisibilityChange,
                label = "Visibility"
            )

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Channel")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateChannelViewPreview() {
    ChImpTheme {
        CreateChannelView( onSubmit = { _, _ -> } )
    }
}