package pt.isel.chimp.service

import kotlinx.coroutines.delay
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
    suspend fun createChannel(name: String, visibility: String, creator: User): Either<ChannelError, Channel>
    suspend fun getChannelById(id: Int, user: User): Either<ChannelError, Channel>
    suspend fun getChannelsByUser(user: User, limit: Int = 10, skip: Int = 0): Either<ChannelError, List<Channel>>
}

sealed class ChannelError(val message: String) {
    data object ChannelNotFoundException : ChannelError("Channel not found")
    data object UserNotFoundException : ChannelError("User not found")
    data object InvalidVisibility : ChannelError("Invalid visibility")
    data object ChannelNameAlreadyExists : ChannelError("Channel name already exists")
}



class MockChannelService : ChannelService {

    private val channels =
        mutableListOf<Channel>(
            Channel(1, "DAW", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(2, "PDM", User(2, "Alice", "alice@example.com"), Visibility.PRIVATE),
            Channel(3, "TVS", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            Channel(4, "SegIngf", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(5, "IPW", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            Channel(6, "PG", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(7, "PSC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            Channel(8, "LSD", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(9, "LIC", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
            Channel(10, "EGP", User(1, "Bob", "bob@example.com"), Visibility.PUBLIC),
            Channel(11, "AED", User(1, "Bob", "bob@example.com"), Visibility.PRIVATE),
        )
    private data class UserRole(val userId: Int, val channelId: Int)
    private val userRoles = mutableListOf<UserRole>(
        UserRole(1, 1),
        UserRole(2, 1),
        UserRole(2, 2),
        UserRole(1, 2),
        UserRole(1, 3),
        UserRole(1, 4),
        UserRole(1, 5),
        UserRole(1, 6),
        UserRole(1, 7),
        UserRole(1, 8),
        UserRole(1, 9),
        UserRole(1, 10),
        UserRole(1, 11),
    )
    private var currentId = 2

    override suspend fun createChannel(name: String, visibility: String, creator: User): Either<ChannelError, Channel> {
        if (channels.any { it.name == name }) {
            return failure(ChannelError.ChannelNameAlreadyExists)
        }
        val channel = Channel(currentId++, name, creator, Visibility.valueOf(visibility))
        channels.add(channel)
        return success(channel)
    }

    override suspend fun getChannelById(id: Int, user: User): Either<ChannelError, Channel> {
        val channel = channels.find {
            it -> it.id == id
                && it.id in userRoles.filter { roleChannel -> roleChannel.userId == user.id }.map { it.channelId } }
        return if (channel != null) {
            success(channel)
        } else {
            failure(ChannelError.ChannelNotFoundException)
        }
    }

    override suspend fun getChannelsByUser(user: User, limit: Int, skip: Int): Either<ChannelError, List<Channel>> {
        val userChannels = userRoles
            .filter { it.userId == user.id }
            .mapNotNull { userRole -> channels.find { it.id == userRole.channelId } }.drop(skip).take(limit)
        delay(500)
        return success(userChannels)
    }
}