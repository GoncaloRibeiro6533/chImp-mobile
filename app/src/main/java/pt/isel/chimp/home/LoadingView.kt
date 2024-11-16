package pt.isel.chimp.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun LoadingView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Pulsating {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_app4),
                contentDescription = "Pulsing Image",
                modifier = Modifier
                    .size(250.dp)
            )
        }
    }
}


// taken from : https://github.com/pauloaapereira/Medium_JetpackCompose_PulsatingEffect/blob/main/app/src/main/java/com/example/androiddevchallenge/MainActivity.kt
@Composable
fun Pulsating(pulseFraction: Float = 1.1f, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(modifier = Modifier.scale(scale)) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingViewPreview() {
    ChImpTheme {
             LoadingView()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoadingViewPreviewDark() {
    LoadingView()
}
