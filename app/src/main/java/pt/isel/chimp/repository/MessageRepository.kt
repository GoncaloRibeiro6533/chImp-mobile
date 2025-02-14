package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.repository.interfaces.MessageRepositoryInterface
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.http.utils.ChImpException
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.MessageEntity
import pt.isel.chimp.storage.entities.UserEntity
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success
import java.time.LocalDateTime
import java.time.ZoneOffset

class MessageRepository(
    private val local: ChImpClientDB,
    private val remote: ChImpService
) : MessageRepositoryInterface {

    override suspend fun insertMessage(messages: List<Message>) {
        val users = messages.map { it.sender }.distinct()
        local.userDao().insertUsers(*users.map { user ->
            UserEntity(
                user.id,
                user.username,
                user.email
            )
        }.toTypedArray())
        local.messageDao().insertMessages(*messages.map {
            MessageEntity(
                it.id,
                it.sender.id,
                it.channel.id,
                it.content,
                it.timestamp.toEpochSecond(ZoneOffset.UTC)

            )
        }.toTypedArray())
    }

    private fun getMessages(channel: Channel): Flow<List<Message>> =
        local.messageDao().getMessagesByChannelId(channel.id).map { list ->
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

        }.distinctUntilChanged()

    override suspend fun channelHasMessages(channel: Channel): Boolean {
        return local.messageDao().nMessagesOfChannel(channel.id) > 0
    }

    private suspend fun getFromAPI(channel: Channel, limit: Int, skip: Int) =
        remote.messageService.getMessagesByChannel(channel.id, limit, skip)

    override suspend fun fetchMessages(channel: Channel, limit: Int, skip: Int): Flow<List<Message>> {
        if (!local.channelDao().isLoaded(channel.id)) {
            when (val result = getFromAPI(channel, limit, skip)) {
                is Success -> {
                    insertMessage(result.value)
                    local.channelDao().markChannelAsLoaded(channel.id)

                }
                is Failure -> throw ChImpException(result.value.message, null)
            }
        }
        return getMessages(channel)
    }

    override suspend fun deleteMessages(channel: Channel) {
        local.messageDao().deleteMessagesOfChannel(channel.id)
    }

    override suspend fun clear() {
        local.messageDao().clear()
    }

     override suspend fun hasMoreMessages(channel: Channel, limit: Int, skip: Int) =
        local.messageDao().nMessagesOfChannel(channel.id) > limit + skip

    override suspend fun loadMoreMessages(channel: Channel, limit: Int, skip: Int) : Either<ApiError, List<Message>> {
        return when (val result = getFromAPI(channel, limit, skip)) {
            is Success -> {
                insertMessage(result.value)
                success(result.value)
            }
            is Failure -> failure(result.value)
        }
    }
}