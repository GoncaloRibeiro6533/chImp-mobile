package pt.isel.chimp.channels.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun MessageView(
    user: User,
    message: String,
    isCurrentUser: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).background(
                if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(8.dp)
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            MessageTextAndTimeElement(
                message = message,
                time = "12:00"
            )
        }
    }
}


@Composable
fun MessageTextAndTimeElement(
    message: String,
    time: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(
            start = 10.dp
        )
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Start,
        )
    }
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp, end = 10.dp)
    ) {
        Text(
            text = time,
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = false)
@Composable
fun MViewPreview() {
    ChImpTheme {
        Column {
            MessageView(
                user = User(1, "Bob", "bob@example.com"),
                message = "Olá, tudo bem?",
                isCurrentUser = false
            )
            MessageView(
                user = User(2, "Alice", "alice@example.com"),
                message = "Very, very, very very ................................................ long message",
                isCurrentUser = true
            )
            MessageView(
                user = User(2, "Alice", "alice@example.com"),
                message = "Very, very, very very ................................................ long message",
                isCurrentUser = false
            )
        }
    }
}
