package pt.isel.chimp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R


@Composable
fun HomePopPup(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()

    ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_app4),
                contentDescription = "ChIMP logo",
                modifier = Modifier.padding(10.dp).size(250.dp),
                alignment = Alignment.Center,
            )
        }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePopPupPreview() {
    HomePopPup()
}
