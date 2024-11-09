package pt.isel.chimp.channels.generalComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun ChannelLogo(name: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(color = calculateLogoColor(name), shape = CircleShape)
    ) {
        Text(
            text = name[0].toString(),
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

private fun calculateLogoColor(string: String): Color {
    val hash = abs(string.hashCode())
    val red = hash % 256
    val green = (hash / 256) % 256
    val blue = (hash / 256 / 256) % 256

    return Color(red, green, blue)
}

@Preview
@Composable
fun ChannelLogoPreview() {
    ChannelLogo("Channel 1")
}