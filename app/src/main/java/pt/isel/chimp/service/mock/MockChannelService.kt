package pt.isel.chimp.service.mock

import kotlinx.coroutines.delay
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success


class MockChannelService(private val repoMock: RepoMock) : ChannelService {

    private suspend fun <T: Any>interceptRequest(token: String, block: suspend (User) -> Either<ApiError, T>): Either<ApiError, T> {
        delay(100)
        val session = repoMock.userRepoMock.findSessionByToken(token) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(ApiError("User not found"))
        return block(user)
    }

    override suspend fun createChannel(creatorToken: String, name: String, visibility: Visibility): Either<ApiError, Channel> =
        interceptRequest<Channel>(creatorToken) { userCreator ->
            delay(1000)
            if (visibility !in Visibility.entries.toTypedArray()) return@interceptRequest failure(ApiError("Invalid visibility"))
            if (repoMock.channelRepoMock.findChannelByName(name, 1, 0).isNotEmpty()) {
                return@interceptRequest failure(ApiError("Channel name already exists"))
            }
            val channel = repoMock.channelRepoMock.createChannel(name, visibility, userCreator)
            return@interceptRequest success(channel)
        }

    override suspend fun getChannelById(id: Int, token: String): Either<ApiError, Channel> =
       interceptRequest<Channel>(token) { user ->
           delay(1000)
           val channel = repoMock.channelRepoMock.findChannelById(id)
               ?: return@interceptRequest failure(ApiError("Channel not found"))
           val userInChannel = repoMock.channelRepoMock.findChannelsOfUser(user)
           if (channel !in userInChannel) {
               return@interceptRequest failure(ApiError("User not in channel"))
           }
           return@interceptRequest success(channel)
       }


    override suspend fun getChannelsByUser(token: String, limit: Int, skip: Int): Either<ApiError, List<Channel>> =
        interceptRequest<List<Channel>>(token) { user ->
            delay(1000)
            if (limit < 0 || skip < 0) return@interceptRequest failure(ApiError("Invalid limit or skip"))
            val userChannels = repoMock.channelRepoMock.findChannelsOfUser(user)
            delay(500)
            return@interceptRequest success(userChannels)
        }

    override suspend fun addUserToChannel(
        token: String,
        userToAdd: Int,
        channelId: Int,
        role: Role,
    ): Either<ApiError, Channel> =
        interceptRequest<Channel>(token) {
            delay(1000)
            if (userToAdd < 0) return@interceptRequest failure(ApiError("Invalid user id"))
            if (channelId < 0) return@interceptRequest failure(ApiError("Invalid channel id"))
            if (role !in Role.entries.toTypedArray()) return@interceptRequest failure(ApiError("Invalid role"))
            val user = repoMock.userRepoMock.findUserById(userToAdd) ?: return@interceptRequest failure(ApiError("User to add not found"))
            val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: return@interceptRequest failure(ApiError("Channel not found"))
            val channelOfUser = repoMock.channelRepoMock.findChannelsOfUser(user)
            if (channel in channelOfUser) return@interceptRequest failure(ApiError("User already in channel"))
            repoMock.channelRepoMock.addUserToChannel(userToAdd, channel, role)
            return@interceptRequest success(channel)
        }

    override suspend fun getChannelMembers(token: String, channel: Channel): Either<ApiError, List<Pair<User, UserInChannel>>> =
        interceptRequest<List<Pair<User, UserInChannel>>>(token) { user ->
            delay(1000)
            val userInChannel = repoMock.channelRepoMock.findChannelsOfUser(user)
            if (channel !in userInChannel) return@interceptRequest failure(ApiError("User not in channel"))
            val usersInChannel = repoMock.channelRepoMock.getChannelMembers(channel)
            val users = mutableListOf<Pair<User, UserInChannel>>()
            usersInChannel.forEach {
                val userChannel = repoMock.userRepoMock.findUserById(it.userId) ?:
                return@interceptRequest failure(ApiError("User not found"))
                users.add(Pair(userChannel, it))
            }
            return@interceptRequest success(users)
        }
}