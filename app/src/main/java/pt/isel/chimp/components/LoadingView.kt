package pt.isel.chimp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.R

const val LOADING_VIEW_TAG = "LoadingView"
const val PROGRESS_INDICATOR_TAG = "progress_indicator"

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = .5F)).testTag(LOADING_VIEW_TAG),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            stringResource(id = R.string.loading_label),
            )
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp).testTag(PROGRESS_INDICATOR_TAG),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoadingViewPreview() {
    ChImpTheme {
        LoadingView()
    }
}
