package pt.isel.chimp.service

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success


/**
 * Contract of the service that provides channels.

 */
interface ChannelService {
    suspend fun createChannel(name: String, visibility: Visibility, creatorId: Int): Either<ChannelError, Channel>
    suspend fun getChannelById(id: Int, user: User): Either<ChannelError, Channel>
    suspend fun getChannelsByUser(user: User, limit: Int = 10, skip: Int = 0): Either<ChannelError, List<Channel>>
    suspend fun addUserToChannel(userToAdd: Int, channelId: Int, role: Role): Either<ChannelError, Channel>
}

sealed class ChannelError(val message: String) {
    data object ChannelNotFoundException : ChannelError("Channel not found")
    data object UserNotFoundException : ChannelError("User not found")
    data object InvalidVisibility : ChannelError("Invalid visibility")
    data object ChannelNameAlreadyExists : ChannelError("Channel name already exists")
    data object UserAlreadyInChannel : ChannelError("User already in channel")
}



class MockChannelService(private val repoMock: RepoMock) : ChannelService {


    override suspend fun createChannel(name: String, visibility: Visibility, creatorId: Int): Either<ChannelError, Channel> {
        if (repoMock.channelRepoMock.findChannelByName(name, 1, 0).isNotEmpty()) {
            return failure(ChannelError.ChannelNameAlreadyExists)
        }
        val creator = repoMock.userRepoMock.findUserById(creatorId) ?: return failure(ChannelError.UserNotFoundException)
        val channel = repoMock.channelRepoMock.createChannel(name, visibility, creator)
        return success(channel)
    }

    override suspend fun getChannelById(id: Int, user: User): Either<ChannelError, Channel> {
        val channel = repoMock.channelRepoMock.findChannelById(id) ?: return failure(ChannelError.ChannelNotFoundException)
        val userInChannel = repoMock.channelRepoMock.findChannelsOfUser(user)
        if (channel !in userInChannel) {
            return failure(ChannelError.ChannelNotFoundException)
        }
        return success(channel)
    }

    override suspend fun getChannelsByUser(user: User, limit: Int, skip: Int): Either<ChannelError, List<Channel>> {
        val userChannels = repoMock.channelRepoMock.findChannelsOfUser(user)
        delay(500)
        return success(userChannels)
    }

    override suspend fun addUserToChannel(
        userToAdd: Int,
        channelId: Int,
        role: Role
    ): Either<ChannelError, Channel> {
        val user = repoMock.userRepoMock.findUserById(userToAdd) ?: return failure(ChannelError.UserNotFoundException)
        val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: return failure(ChannelError.ChannelNotFoundException)
        val channelOfUser = repoMock.channelRepoMock.findChannelsOfUser(user)
        if (channel in channelOfUser) {
            return failure(ChannelError.UserAlreadyInChannel)
        }
        repoMock.channelRepoMock.addUserToChannel(userToAdd, channel, role)
        return success(channel)
    }
}