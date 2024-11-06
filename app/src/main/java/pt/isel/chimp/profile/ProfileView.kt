package pt.isel.chimp.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ProfileView(
    profile: Profile,
    onEditUsernameClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile icon",
            modifier = Modifier.size(80.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Username: ${profile.username}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .widthIn(max = 200.dp)
            )
            IconButton(
                onClick = onEditUsernameClick,
                modifier = Modifier.size(48.dp).background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit icon",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Text(
            text = "Email: ${profile.email}",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {
    ChImpTheme {
        ProfileView(
            profile = Profile(
            username = "Alice",
            email = "alice@example.com"
            ),
         {  }
        )
    }
}