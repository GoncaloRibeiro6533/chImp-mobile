package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.MessageEntity
import pt.isel.chimp.storage.entities.UserEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

class MessageRepository(
    private val db: ChImpClientDB
) {

    suspend fun insertMessage(messages: List<Message>) {
        val users = messages.map { it.sender }.distinct()
        db.userDao().insertUsers(*users.map { user ->
            UserEntity(
                user.id,
                user.username,
                user.email
            )
        }.toTypedArray())
        db.messageDao().insertMessages(*messages.map {
            MessageEntity(
                it.id,
                it.sender.id,
                it.channel.id,
                it.content,
                it.timestamp.toEpochSecond(ZoneOffset.UTC)

            )
        }.toTypedArray())
    }

    fun getMessages(channel: Channel): Flow<List<Message>> {
        val a= db.messageDao().getMessagesByChannelId(channel.id).map { list ->
            list.map { it ->
                Message(
                    id = it.message.id,
                    sender = User(it.sender.id, it.sender.username, it.sender.email),
                    channel = Channel(
                        channel.id,
                        channel.name,
                        channel.creator,
                        channel.visibility
                        ),
                    content = it.message.content,
                    timestamp = LocalDateTime.ofEpochSecond(it.message.timestamp, 0, ZoneOffset.UTC)
                )
            }

        }
        return a.distinctUntilChanged()
    }

    suspend fun clear() {
        db.messageDao().clear()
    }
}