package pt.isel.chimp.ui.screens.invitationList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

@Composable
fun InvitationListView(
    invitations: StateFlow<List<Invitation>>,
    onAccept: (Invitation) -> Unit,
    onDecline: (Invitation) -> Unit
) {
    val invitationsList = invitations.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Channel Invitations",
            modifier = Modifier.padding(10.dp),
            fontSize = 30.sp
        )

        Spacer(Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (invitationsList.isEmpty()) {

                item {

                    Spacer(Modifier.height(250.dp))

                    Text(
                        text = "No invitations waiting for approval.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray

                    )
                }

            }
            else {
                items(invitationsList) { invitation ->
                    InvitationCard(
                        invitation = invitation,
                        onAccept = { onAccept(invitation) },
                        onDecline = { onDecline(invitation) }
                    )
                }
            }


        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvitationListPreview() {

    val user1 = User(1, "Bob", "bob@example.com")
    val user2 = User(2, "Alice", "alice@example.com")
    val user3 = User(3, "Charlie", "charlie@example.com")

    val channel1 = Channel(
        id = 1,
        name = "Bob's Channel",
        creator = user1,
        visibility = Visibility.PUBLIC
    )

    val channel2 = Channel(
        id = 2,
        name = "Alice's Channel",
        creator = user2,
        visibility = Visibility.PUBLIC
    )

    val inv1 = Invitation(
        id = 1,
        sender = user1,
        receiver = user3,
        channel = channel1,
        role = Role.READ_WRITE,
        isUsed = false,
        timestamp = LocalDateTime.now()
    )

    val inv2 = Invitation(
        id = 2,
        sender = user2,
        receiver = user3,
        channel = channel2,
        role = Role.READ_ONLY,
        isUsed = false,
        timestamp = LocalDateTime.now()
    )

    val invList =  listOf(inv1, inv2)
    //val invList = listOf<Invitation>()

    InvitationListView(
        invitations = MutableStateFlow(invList),
        onAccept = { },
        onDecline = { }
    )
}