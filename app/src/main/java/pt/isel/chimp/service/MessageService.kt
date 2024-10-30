package pt.isel.chimp.service

import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.service.repo.RepoMockImpl
import java.time.LocalDateTime

interface MessageService {

}
class MockMessageService(private val repoMock: RepoMock) : MessageService {

    private val senderBob = User(1, "Bob", "bob@example.com")
    private val senderAlice = User(2, "Alice", "alice@example.com")
    private val channel = Channel(1, "DAW", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC)


    private val messages =
        mutableListOf(
            Message(1, senderBob, channel,"Hello, Alice!",
                LocalDateTime.of(2024, 10, 29, 12, 0)),
            Message(2, senderAlice, channel,"Hello, Bob!", LocalDateTime.of(2024, 10, 29, 12, 1)),
            Message(3, senderBob, channel,"How are you?", LocalDateTime.of(2024, 10, 29, 12, 2)),
            Message(4, senderAlice, channel,"I'm fine, thank you!", LocalDateTime.of(2024, 10, 29, 12, 3)),
        )
    private var currentId = 4

    suspend fun createMessage(senderId: Int, channelId: Int, content: String): Message {
        val sender = if(senderId == 1) senderBob else senderAlice
        val message = Message(currentId++, sender, channel, content, LocalDateTime.now())
        messages.add(message)
        return message
    }

    suspend fun getMessagesByChannel(channelId: Int, limit: Int, skip: Int): List<Message> {
        return messages.filter { it.channel.id == channelId }.sortedBy { it.timestamp }
            .drop(skip)
            .take(limit)

    }
}