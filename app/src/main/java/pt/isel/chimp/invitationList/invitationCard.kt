package pt.isel.chimp.invitationList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.channels.generalComponents.ChannelLogo
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.ui.theme.ChImpTheme
import pt.isel.chimp.utils.RoundedRectangleWithText
import java.time.LocalDateTime

@Composable
fun InvitationCard(
    invitation: Invitation,
    onAccept: (Int) -> Unit,
    onDecline: (Int) -> Unit
) {
    val sender = invitation.sender.username
    val greenColor = Color(0xFF5CB338)
    val redColor = Color.Red
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChannelLogo(invitation.channel.name)
                Spacer(Modifier.padding(10.dp))
                Column {
                    Text(text = invitation.channel.name)
                    Text(text = "From: $sender", fontSize = 10.sp)
                }
            }
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.End
            ) {

                RoundedRectangleWithText(
                    text = invitation.role.value,
                    backgroundColor = if (invitation.role == Role.READ_WRITE) Color.Red else Color.Green,
                )
                Spacer(Modifier.width(16.dp))
                IconButton(
                    onClick = { onDecline(invitation.id) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Decline Invitation",
                        tint = redColor
                    )
                }

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = { onAccept(invitation.id) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Accept Invitation",
                        tint = greenColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun InvitationCardPreview() {

    val channel = Channel(
        id = 1,
        name = "Channel name",
        creator = User(1, "Bob", "bob@example.com"),
        visibility = Visibility.PUBLIC
    )

    val inv1 = Invitation(
        id = 1,
        sender = User(1, "Bob", "bob@example.com"),
        receiver = User(2, "Alice", "alice@example.com"),
        channel = channel,
        role = Role.READ_WRITE,
        isUsed = false,
        timestamp = LocalDateTime.now()
    )

    ChImpTheme {
        Column {
            InvitationCard(inv1, {}, {})
            Spacer(Modifier.padding(5.dp))
            InvitationCard(inv1, {}, {})
            Spacer(Modifier.padding(5.dp))
            InvitationCard(inv1, {}, {})
        }
    }
}