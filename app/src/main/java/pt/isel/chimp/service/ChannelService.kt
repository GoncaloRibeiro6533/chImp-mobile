package pt.isel.chimp.service

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success


/**
 * Contract of the service that provides channels.

 */
interface ChannelService {
    suspend fun createChannel(name: String, visibility: String, creatorId: Int): Either<ChannelError, Channel>
    suspend fun getChannelById(id: Int, user: User): Either<ChannelError, Channel>
    suspend fun getChannelsByUser(user: User, limit: Int = 10, skip: Int = 0): Either<ChannelError, List<Channel>>
    suspend fun addUserToChannel(userToAdd: Int, channel: Channel, role: Role): Either<ChannelError, Channel>
}

sealed class ChannelError(val message: String) {
    data object ChannelNotFoundException : ChannelError("Channel not found")
    data object UserNotFoundException : ChannelError("User not found")
    data object InvalidVisibility : ChannelError("Invalid visibility")
    data object ChannelNameAlreadyExists : ChannelError("Channel name already exists")
    data object UserAlreadyInChannel : ChannelError("User already in channel")
}



class MockChannelService : ChannelService {

    private val channels =
        mutableListOf<Channel>(
            Channel(1, "DAW", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(2, "PDM", User(2, "Alice", "alice@example.com"), Visibility.PRIVATE),
            Channel(3, "TVS", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            /*Channel(4, "SegIngf", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(5, "IPW", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            Channel(6, "PG", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(7, "PSC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            Channel(8, "LSD", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(9, "LIC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            Channel(10, "EGP", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(11, "AED", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),

             */
        )
    private data class UserInChannel(val userId: Int, val channelId: Int, val role: Role)
    private val userInChannel = mutableListOf<UserInChannel>(
        UserInChannel(1, 1, Role.READ_WRITE),
        UserInChannel(2, 1, Role.READ_WRITE),
        UserInChannel(2, 2, Role.READ_WRITE),
        UserInChannel(1, 2, Role.READ_WRITE),
        UserInChannel(1, 3, Role.READ_WRITE),
        /*UserInChannel(1, 4),
        UserInChannel(1, 5),
        UserInChannel(1, 6),
        UserInChannel(1, 7),
        UserInChannel(1, 8),
        UserInChannel(1, 9),
        UserInChannel(1, 10),
        UserInChannel(1, 11),

         */
    )

    private var currentId = 50

    override suspend fun createChannel(name: String, visibility: String, creatorId: Int): Either<ChannelError, Channel> {
        if (channels.any { it.name == name }) {
            return failure(ChannelError.ChannelNameAlreadyExists)
        }
        val creatorID = MockUserService().findUserById(creatorId)
        val channel = Channel(currentId++, name, creatorID, Visibility.valueOf(visibility))
        channels.add(channel)
        addUserToChannel(creatorId, channel, Role.READ_WRITE)
        return success(channel)
    }

    override suspend fun getChannelById(id: Int, user: User): Either<ChannelError, Channel> {
        val channel = channels.find {
            it -> it.id == id
                && it.id in userInChannel.filter { roleChannel -> roleChannel.userId == user.id }.map { it.channelId } }
        return if (channel != null) {
            success(channel)
        } else {
            failure(ChannelError.ChannelNotFoundException)
        }
    }

    override suspend fun getChannelsByUser(user: User, limit: Int, skip: Int): Either<ChannelError, List<Channel>> {
        val userChannels = userInChannel
            .filter { it.userId == user.id }
            .mapNotNull { userRole -> channels.find { it.id == userRole.channelId } }.drop(skip).take(limit)
        delay(500)
        return success(userChannels)
    }

    override suspend fun addUserToChannel(
        userToAdd: Int,
        channel: Channel,
        role: Role
    ): Either<ChannelError, Channel> {
        if (channels.find { it.id == channel.id } == null) {
            return failure(ChannelError.ChannelNotFoundException)
        }
        if (userInChannel.find { it.userId == userToAdd && it.channelId == channel.id } != null) {
            return failure(ChannelError.UserAlreadyInChannel)
        }
        userInChannel.add(UserInChannel(userToAdd, channel.id, role))
        return success(channel)
    }
}