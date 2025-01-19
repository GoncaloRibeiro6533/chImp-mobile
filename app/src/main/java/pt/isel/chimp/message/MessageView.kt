package pt.isel.chimp.message

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.ui.theme.ChImpTheme
import java.time.LocalDateTime

@Composable
fun MessageView(
    user: User,
    message: Message,
    isCurrentUser: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = messageColor(isCurrentUser)
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
                message = message.content,
                time = message.timestamp
            )
        }
    }
}

@Composable
private fun messageColor(isCurrentUser: Boolean) : CardColors =
    if (isCurrentUser) {
        CardColors(
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.DarkGray,
        )
    } else {
        CardColors(
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color.DarkGray,
        )
    }


@Composable
fun MessageTextAndTimeElement(
    message: String,
    time: LocalDateTime
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, end = 10.dp)
    ) {
        Text(
            text = time.getHourAndMinutes(),
            textAlign = TextAlign.End
        )
    }
}

private fun LocalDateTime.getHourAndMinutes(): String {
    val hour = hour.toString().padStart(2, '0')
    val minute = minute.toString().padStart(2, '0')
    return "$hour:$minute"
}


private val user = User(1, "Bob", "bob@example.com")
private val channel = Channel(1, "DAW", user, Visibility.PUBLIC)
private val message = Message(
    1,
    user,
    channel,
    "Hello, Alice!",
    LocalDateTime.of(2021, 10, 1, 10, 0)
)

private val longMessage = Message(
    1,
    user,
    channel,
    "Very, very, very very ................................................ long message",
    LocalDateTime.of(2021, 10, 1, 10, 0)
)

@Preview(showBackground = false)
@Composable
fun MViewPreview() {
    ChImpTheme {
        Column {
            MessageView(
                user,
                message,
            )
            MessageView(
                user,
                longMessage
            )
        }
    }
}
