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
                channel.visibility.value,
                channels[channel]?.value ?: Role.READ_WRITE.value
            )
        }.toTypedArray())
        db.channelDao().insertUserInChannel(*channels.map { (channel, role) ->
            UserInChannel(
                userId,
                channel.id,
                role.value
            )
        }.toTypedArray())

    }

    suspend fun clear() {
        db.channelDao().clearUserInChannel()
        db.channelDao().clear()
    }
}