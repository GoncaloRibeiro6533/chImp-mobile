package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.UserEntity
import pt.isel.chimp.storage.entities.UserInChannel

class ChannelRepository(
    private val db: ChImpClientDB
) {
    fun getChannels(): Flow<Map<Channel, Role>> {
        return db.channelDao().getChannels().map { channels ->
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

    suspend fun insertChannels(userId: Int, channels: Map<Channel, Role>) {
        db.userDao().insertUsers(*channels.keys.map{ channel ->
            UserEntity(
                channel.creator.id,
                channel.creator.username,
                channel.creator.email
            )
        }.toTypedArray())
        db.channelDao().insertChannels(*channels.keys.map { channel ->
                ChannelEntity(
                channel.id,
                channel.name,
                channel.creator.id,
                channel.visibility.name,
                channels[channel]?.name ?: Role.READ_WRITE.name
            )
        }.toTypedArray())
        db.channelDao().insertUserInChannel(*channels.map { (channel, role) ->
            UserInChannel(
                userId,
                channel.id,
                role.name
            )
        }.toTypedArray())

    }

    suspend fun updateChannel(channel: Channel) {
        db.channelDao().updateChannelName(channel.id, channel.name)
    }

    suspend fun insertUserInChannel(userId: Int, channelId: Int, role: Role) {
        db.channelDao().insertUserInChannel(UserInChannel(userId, channelId, role.name))
    }

    suspend fun removeUserFromChannel(userId: Int, channelId: Int) {
        db.channelDao().deleteUserInChannel(userId, channelId)
        db.channelDao().deleteChannel(channelId)
    }
    suspend fun clear() {
        db.channelDao().clearUserInChannel()
        db.channelDao().clear()
    }

    fun getChannelMembers(channel: Channel) : Flow<Map<User, Role>> {
        return db.channelDao().getChannelMembers(channel.id).map { users ->
            users.map { user ->
                User(
                    user.user.id,
                    user.user.username,
                    user.user.email
                ) to Role.valueOf(user.userInChannel.role)
            }.toMap()
        }
    }

    fun getAllChannels(): Flow<List<Channel>> {
        return db.channelDao().getAllChannels().map { channels ->
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
                )
            }
        }
    }
}