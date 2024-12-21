package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.MessageEntity
import pt.isel.chimp.storage.entities.UserEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

class MessageRepository(
    private val db: ChImpClientDB
) {

    suspend fun insertMessage(messages: List<Message>) {
        db.userDao().insertUsers(*messages.map {  //TODO: improve this
            UserEntity(
                it.sender.id,
                it.sender.username,
                it.sender.email
            )
        }.toTypedArray())
        db.channelDao().insertChannels(messages.first().channel.let {
            ChannelEntity(
                it.id,
                it.name,
                it.creator.id,
                it.visibility
            )
        })
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
        return db.messageDao().getMessagesByChannelId(channel.id).map { list ->
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
    }

    suspend fun clear() {
        db.messageDao().clear()
    }
}