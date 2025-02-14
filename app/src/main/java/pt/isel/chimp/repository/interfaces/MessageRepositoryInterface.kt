package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.utils.Either

interface MessageRepositoryInterface {
    suspend fun fetchMessages(channel: Channel, limit: Int, skip: Int): Flow<List<Message>>
    suspend fun insertMessage(messages: List<Message>)
    suspend fun channelHasMessages(channel: Channel): Boolean
    suspend fun deleteMessages(channel: Channel)
    suspend fun clear()
    suspend fun loadMoreMessages(channel: Channel, limit: Int, skip: Int): Either<ApiError,List<Message>>
    suspend fun hasMoreMessages(channel: Channel, limit: Int, skip: Int) : Boolean
}