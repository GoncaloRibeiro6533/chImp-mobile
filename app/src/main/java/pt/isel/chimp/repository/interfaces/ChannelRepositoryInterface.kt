package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User

interface ChannelRepositoryInterface {
    fun getChannels(): Flow<Map<Channel, Role>>
    suspend fun insertChannels(userId: Int, channels: Map<Channel, Role>)
    suspend fun updateChannel(channel: Channel)
    suspend fun insertUserInChannel(userId: Int, channelId: Int, role: Role)
    suspend fun removeUserFromChannel(userId: Int, channelId: Int)
    suspend fun clear()
    fun getChannelMembers(channel: Channel) : Flow<Map<User, Role>>
    fun getAllChannels(): Flow<List<Channel>>
    suspend fun markChannelAsLoaded(channelId: Int)
    suspend fun isLoaded(channelId: Int): Boolean
}