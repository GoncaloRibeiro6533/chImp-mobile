package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.utils.Either

interface ChannelRepositoryInterface {
    suspend fun fetchChannels(user: User, limit: Int, skip: Int) : Flow<Map<Channel, Role>>
    suspend fun insertChannels(userId: Int, channels: Map<Channel, Role>)
    suspend fun updateChannel(channel: Channel)
    suspend fun insertUserInChannel(userId: Int, channelId: Int, role: Role)
    suspend fun removeUserFromChannel(userId: Int, channelId: Int)
    suspend fun clear()
    suspend fun findByName(name: String, limit: Int, skip: Int) : Either<ApiError, List<Channel>>
    suspend fun markChannelAsLoaded(channelId: Int)
    suspend fun isLoaded(channelId: Int): Boolean
    suspend fun createChannel(name: String, creatorId: Int, visibility: Visibility): Either<ApiError, Channel>
    suspend fun changeChannelName(channel: Channel, name: String): Either<ApiError, Channel>
    suspend fun leaveChannel(channel: Channel, user: User): Either<ApiError, Channel>
    suspend fun fetchChannelMembers(channel: Channel): Flow<Map<User, Role>>
    fun getChannel(channel: Channel): Flow<Channel>
    suspend fun joinChannel(user: User, channel: Channel, role: Role) : Either<ApiError, Channel>
}