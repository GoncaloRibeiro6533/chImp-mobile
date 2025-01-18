package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message

interface MessageRepositoryInterface {

    fun getMessages(channel: Channel): Flow<List<Message>>
    suspend fun insertMessage(messages: List<Message>)
    suspend fun channelHasMessages(channel: Channel): Boolean
    suspend fun deleteMessages(channel: Channel)
    suspend fun clear()
}