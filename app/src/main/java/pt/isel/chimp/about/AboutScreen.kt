package pt.isel.chimp.about

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.R
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme


/**
 * Tags used to identify the components of the AboutScreen in automated tests
 */
const val ABOUT_SCREEN_TEST_TAG = "AboutScreenTestTag"
const val AUTHOR_INFO_TEST_TAG = "AuthorInfoTestTag"
const val SOCIALS_ELEMENT_TEST_TAG = "SocialsElementTestTag"
const val NAVIGATE_BACK_TEST_TAG = "NavigateBack"

/**
 * Root composable for the about screen, the one that displays information about the app.
 * @param onNavigateBack the callback to be invoked when the user requests to go back to the
 * previous screen
 * @param onSendEmailRequested the callback to be invoked when the user requests to send an email
 * @param onOpenUrlRequested the callback to be invoked when the user requests to open an url
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit = { },
    onSendEmailRequested: (String) -> Unit = { },
    onOpenUrlRequested: (Uri) -> Unit = { }
) {
    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(ABOUT_SCREEN_TEST_TAG),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(text ="About", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                defaultAuthors.forEach { author ->
                    Author(author, onSendEmailRequested, onOpenUrlRequested)
                }
            }
        }
    }
}

/**
 * Composable used to display information about the author of the application
 */
@Composable
private fun Author(author: CreatorInfo, onSendEmailRequested: (String) -> Unit = { },onOpenUrlRequested: (Uri) -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { onSendEmailRequested(author.email) }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user_img),
                contentDescription = null,
                modifier = Modifier.sizeIn(50.dp, 50.dp, 100.dp, 100.dp)
            )
            Text(text = author.name, style = MaterialTheme.typography.titleLarge)
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            author.socials.forEach {
                Social(id = it.imageId, onClick = { onOpenUrlRequested(it.link)})
            }

        }
    }
}

@Composable
private fun Social(@DrawableRes id: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = Modifier
            .sizeIn(maxWidth = 64.dp)
            .testTag(SOCIALS_ELEMENT_TEST_TAG)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InfoScreenPreview() {
    val socialsPreview = listOf(
        CreatorInfo(
            name = "Tiago Silva",
            imageId = R.drawable.ic_user_img,
            socials = socialsDefault("tiago15ts"),
            email = "A48252@alunos.isel.pt"
        ),
        CreatorInfo(
            name = "Gonçalo Ribeiro",
            imageId = R.drawable.ic_user_img,
            socials = socialsDefault("GoncaloRibeiro6533"),
            email = "A48305@alunos.isel.pt"
        ),
        CreatorInfo(
            name = "Name",
            imageId = R.drawable.ic_user_img,
            socials = socialsDefault("name"),
            email = "A48xxx@alunos.isel.pt"
        )
    )
    AboutScreen()
}