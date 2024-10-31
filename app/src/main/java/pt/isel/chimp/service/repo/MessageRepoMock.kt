package pt.isel.chimp.service.repo

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