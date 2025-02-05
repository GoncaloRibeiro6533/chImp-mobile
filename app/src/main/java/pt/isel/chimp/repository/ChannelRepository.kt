package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.repository.interfaces.ChannelRepositoryInterface
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.http.utils.ChImpException
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.UserEntity
import pt.isel.chimp.storage.entities.UserInChannel
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class ChannelRepository(
    private val local: ChImpClientDB,
    private val remote: ChImpService
) : ChannelRepositoryInterface {

    private fun getChannels(): Flow<Map<Channel, Role>> {
        return local.channelDao().getChannels().map { channels ->
            channels.map { channel ->
                Channel(
                    channel.channel.id,
                    channel.channel.name,
                    User(
                        channel.creator.id,
                        channel.creator.username,
                        channel.creator.email
                    ),
                    Visibility.valueOf(channel.channel.visibility)
                ) to Role.valueOf(channel.channel.role)
            }.toMap()
        }
    }

    override suspend fun insertChannels(userId: Int, channels: Map<Channel, Role>) {
        local.userDao().insertUsers(*channels.keys.map{ channel ->
            UserEntity(
                channel.creator.id,
                channel.creator.username,
                channel.creator.email
            )
        }.toTypedArray())
        local.channelDao().insertChannels(*channels.keys.map { channel ->
            ChannelEntity(
                channel.id,
                channel.name,
                channel.creator.id,
                channel.visibility.name,
                channels[channel]?.name ?: Role.READ_WRITE.name
            )
        }.toTypedArray())
        local.channelDao().insertUserInChannel(*channels.map { (channel, role) ->
            UserInChannel(
                userId,
                channel.id,
                role.name
            )
        }.toTypedArray())

    }

    override suspend fun updateChannel(channel: Channel) {
        local.channelDao().updateChannelName(channel.id, channel.name)
    }

    override suspend fun insertUserInChannel(userId: Int, channelId: Int, role: Role) {
        local.channelDao().insertUserInChannel(UserInChannel(userId, channelId, role.name))
    }

    override suspend fun removeUserFromChannel(userId: Int, channelId: Int) {
        local.channelDao().deleteUserInChannel(userId, channelId)
        local.channelDao().deleteChannel(channelId)
    }
    override suspend fun clear() {
        local.channelDao().clearUserInChannel()
        local.channelDao().clear()
    }

    private fun getChannelMembers(channel: Channel) : Flow<Map<User, Role>> {
        return local.channelDao().getChannelMembers(channel.id).map { users ->
            users.map { user ->
                User(
                    user.user.id,
                    user.user.username,
                    user.user.email
                ) to Role.valueOf(user.userInChannel.role)
            }.toMap()
        }
    }

    override suspend fun markChannelAsLoaded(channelId: Int) {
        local.channelDao().markChannelAsLoaded(channelId)
    }

    override suspend fun isLoaded(channelId: Int): Boolean {
        return local.channelDao().isLoaded(channelId)
    }

    private suspend fun hasChannels(): Boolean {
        return local.channelDao().hasChannels()
    }

    private suspend fun getFromAPI(user: User, limit: Int, skip: Int) =
        remote.channelService.getChannelsOfUser(user.id, limit, skip)


    override suspend fun fetchChannels(user: User, limit: Int, skip: Int) : Flow<Map<Channel, Role>> {
        if (!hasChannels()) {
            when(val channels = getFromAPI(user, limit, skip)) {
                is Success -> insertChannels(user.id, channels.value)
                is Failure -> throw ChImpException(channels.value.message, null)
            }
        }
        return getChannels()
    }

    override suspend fun createChannel(name: String, creatorId: Int, visibility: Visibility): Either<ApiError, Channel> =
        when(val channel = remote.channelService.createChannel(name, creatorId, visibility)) {
            is Success -> {
                insertChannels(creatorId, mapOf(channel.value to Role.READ_WRITE))
                success(channel.value)
            }
            is Failure -> failure(channel.value)
        }

    override suspend fun changeChannelName(channel: Channel, name: String): Either<ApiError, Channel> =
        when(val channel = remote.channelService.updateChannelName(channel.id, name)) {
            is Success -> {
                updateChannel(channel.value)
                success(channel.value)
            }
            is Failure -> failure(channel.value)
        }

    override suspend fun leaveChannel(channel: Channel, user: User): Either<ApiError, Channel> =
        when(val serviceChannel = remote.channelService.removeUserFromChannel(channel.id, user.id)) {
            is Success -> {
                removeUserFromChannel(user.id, channel.id)
                success(serviceChannel.value)
            }
            is Failure -> failure(serviceChannel.value)
        }

    private suspend fun areMembersLoaded(channel: Channel): Boolean {
        return local.channelDao().nMembers(channel.id) > 1
    }

    override suspend fun fetchChannelMembers(channel: Channel): Flow<Map<User, Role>> {
        if (!areMembersLoaded(channel)) {
            when (val result = remote.channelService.getChannelMembers(channel.id)) {
                is Success -> result.value.forEach { member ->
                    local.channelDao().insertUserInChannel(
                        UserInChannel(
                            member.first.id,
                            channel.id,
                            member.second.name
                        )
                    )
                }
                is Failure -> throw ChImpException(result.value.message, null)
            }
        }
        return getChannelMembers(channel)
    }

    override fun getChannel(channel: Channel): Flow<Channel> {
        return local.channelDao().getChannelById(channel.id).map { result ->
            Channel(
                result.id,
                result.name,
                channel.creator,
                Visibility.valueOf(result.visibility)
            )
        }
    }

    private suspend fun getAll() = local.channelDao().getAllChannels().map { channel ->
        Channel(
            channel.channel.id,
            channel.channel.name,
            User(
                channel.creator.id,
                channel.creator.username,
                channel.creator.email
            ),
            Visibility.valueOf(channel.channel.visibility)
        )
    }

    override suspend fun findByName(name: String, limit: Int, skip: Int) : Either<ApiError, List<Channel>> {
        val local = getAll()
        return when(val channels = remote.channelService.searchChannelByName(name, limit, skip)) {
            is Success -> success(channels.value.filter { it !in local })
            is Failure -> failure(channels.value)
        }
    }

    override suspend fun joinChannel(user: User, channel: Channel, role: Role) =
        when(val result = remote.channelService.joinChannel(user.id, channel.id, role)) {
            is Success -> {
                insertChannels(user.id, mapOf(channel to role))
                insertUserInChannel(user.id, channel.id, role)
                success(result.value)
            }
            is Failure -> failure(result.value)
        }

}