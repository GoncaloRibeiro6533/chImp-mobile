package pt.isel.chimp.service.mock.repo

import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

class MessageRepoMock {

    companion object {
        private val senderBob = User(1, "Bob", "bob@example.com")
        private val senderAlice = User(2, "Alice", "alice@example.com")
        private val channel =
            Channel(1, "DAW", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC)


        private val messages =
            mutableListOf(
                Message(
                    1, senderBob, channel, "Hello, Alice!",
                    LocalDateTime.of(2024, 10, 29, 12, 0)
                ),
                Message(
                    2,
                    senderAlice,
                    channel,
                    "Hello, Bob!",
                    LocalDateTime.of(2024, 10, 29, 12, 1)
                ),
                Message(
                    3,
                    senderBob,
                    channel,
                    "How are you?",
                    LocalDateTime.of(2024, 10, 29, 12, 2)
                ),
                Message(
                    4,
                    senderAlice,
                    channel,
                    "I'm fine, thank you!",
                    LocalDateTime.of(2024, 10, 29, 12, 3)
                ),
                Message(
                    5,
                    senderBob,
                    channel,
                    "I'm glad to hear that!",
                    LocalDateTime.of(2024, 10, 29, 12, 4)
                ),
                Message(
                    6,
                    senderAlice,
                    channel,
                    "What are you doing?",
                    LocalDateTime.of(2024, 10, 29, 12, 5)
                ),
                Message(
                    7,
                    senderBob,
                    channel,
                    "I'm working on the project",
                    LocalDateTime.of(2024, 10, 29, 12, 6)
                ),
                Message(
                    8,
                    senderAlice,
                    channel,
                    "I'm doing the same",
                    LocalDateTime.of(2024, 10, 29, 12, 7)
                ),
                Message(
                    9,
                    senderBob,
                    channel,
                    "We should meet to discuss it",
                    LocalDateTime.of(2024, 10, 29, 12, 8)
                ),
                Message(
                    10,
                    senderAlice,
                    channel,
                    "I agree",
                    LocalDateTime.of(2024, 10, 29, 12, 9)
                ),

            )
        private var currentId = 4
    }

    fun createMessage(creator: User, channel: Channel, content: String, time: LocalDateTime): Message {
        val message = Message(currentId++, creator, channel, content, time)
        messages.add(message)
        return message
    }

    fun findMessagesByChannel(channel: Channel, limit: Int, skip: Int): List<Message> {
        return messages.filter { it.channel.id == channel.id }.sortedBy { it.timestamp }
            .drop(skip)
            .take(limit)
    }

    fun findMessageById(id: Int): Message? {
        return messages.find { it.id == id }
    }
}