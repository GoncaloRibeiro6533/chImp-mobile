package pt.isel.chimp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.R
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.ui.theme.ChImpTheme

const val PROFILE_VIEW_TAG = "ProfileView"
const val USERNAME_TEXT_TAG = "UsernameText"
const val EMAIL_TEXT_TAG = "EmailText"
const val EDIT_USERNAME_BUTTON_TAG = "EditUsernameButton"

@Composable
fun ProfileView(
    state: ProfileScreenState.Success,
    onEditUsernameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val orientation = LocalConfiguration.current.orientation

    if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
        // Portrait Layout
        PortraitProfileLayout(
            state = state,
            onEditUsernameClick = onEditUsernameClick,
            modifier = modifier
        )
    } else {
        // Landscape Layout
        LandscapeProfileLayout(
            state = state,
            onEditUsernameClick = onEditUsernameClick,
            modifier = modifier
        )
    }
}

@Composable
fun PortraitProfileLayout(
    state: ProfileScreenState.Success,
    onEditUsernameClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag(PROFILE_VIEW_TAG),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = stringResource(R.string.profile_icon_description),
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .padding(16.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetails(
            state = state,
            onEditUsernameClick = onEditUsernameClick
        )
    }
}

@Composable
fun LandscapeProfileLayout(
    state: ProfileScreenState.Success,
    onEditUsernameClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag(PROFILE_VIEW_TAG),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = stringResource(R.string.profile_icon_description),
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .padding(16.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            ProfileDetails(
                state = state,
                onEditUsernameClick = onEditUsernameClick
            )
        }
    }
}

@Composable
fun ProfileDetails(
    state: ProfileScreenState.Success,
    onEditUsernameClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.username_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = state.profile.username,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .testTag(USERNAME_TEXT_TAG),
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onEditUsernameClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Transparent)
                    .testTag(EDIT_USERNAME_BUTTON_TAG)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_icon_description),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.email_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = state.profile.email,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .testTag(EMAIL_TEXT_TAG),
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {
    ChImpTheme {
        ProfileView(
            state = ProfileScreenState.Success( Profile(
                username = "Alice",
                email = "alicemail@exampledomain.com"
            )),
            onEditUsernameClick = {  }
        )
    }
}