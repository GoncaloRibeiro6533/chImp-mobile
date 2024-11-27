package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.RoundedRectangleWithText

@Composable
fun MemberView(
    user: User,
    role: Role,
    channelCreator: User
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .fillMaxHeight(),
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

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                /*todo add tag creator to the user that created the channel
                 *  (user.id == channel.creatorId)
                 */
                val darkGreen = Color(0xFF006400)
                val redColor = Color.Red
                val roleLabel: String
                val roleColor: Color

                if (user.id == channelCreator.id) {
                    roleLabel = "Creator"
                    roleColor = redColor
                } else {
                    roleLabel = when (role) {
                        Role.READ_ONLY -> "Read Only"
                        Role.READ_WRITE -> "Read/Write"
                    }
                    roleColor = if (role == Role.READ_ONLY) Color.DarkGray else darkGreen
                }
                RoundedRectangleWithText(
                    text = roleLabel,
                    backgroundColor = roleColor
                )
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
                role = Role.READ_WRITE,
                channelCreator = User(1, "Bob", "bob@example.com")
            )
            MemberView(
                user = User(1, "joao", "joao@example.com"),
                role = Role.READ_ONLY,
                channelCreator = User(2, "Alice", "alice@example.com")
            )
            MemberView(
                user = User(2, "Alice", "alice@example.com"),
                role = Role.READ_WRITE,
                channelCreator = User(2, "Alice", "alice@example.com")
            )
        }
    }
}