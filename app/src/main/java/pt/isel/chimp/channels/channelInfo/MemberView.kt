package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.simulateHotReload
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.message.MessageView
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun MemberView(
    user: User,
    role: Role
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(MaterialTheme.colorScheme.primary)
            .height(60.dp),
        shape = RoundedCornerShape(8.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile icon",
                    modifier = Modifier.size(40.dp)
                )
                Column {
                    Text(
                        text = user.username,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 8.dp) // Adding padding to the start for spacing
                    )
                }
            }

            Column {
                (if (role == Role.ADMIN) "Admin" else null)?.let {
                    Text(
                        text = it,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                    )
                }
            }
        }

    }


}

@Preview(showBackground = false)
@Composable
fun MemberViewPreview() {

    ChImpTheme {
        Column {
            MemberView(
                user = User(1, "Bob", "bob@example.com"),
                role = Role.ADMIN
            )
            MemberView(
                user = User(1, "Bob", "bob@example.com"),
                role = Role.ADMIN
            )
            MemberView(
                user = User(1, "Bob", "bob@example.com"),
                role = Role.READ_ONLY
            )
        }

    }
}