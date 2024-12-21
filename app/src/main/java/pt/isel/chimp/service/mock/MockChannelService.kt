package pt.isel.chimp.service.mock

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Url
import kotlinx.coroutines.delay
import pt.isel.chimp.ChImpApplication
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.repo.ChannelRepoMock.Companion.channels
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success


class MockChannelService(
    private val repoMock: RepoMock,
    private val cookieStorage: CookiesStorage
    ) : ChannelService {

    private suspend fun <T: Any>interceptRequest(block: suspend (User) -> Either<ApiError, T>): Either<ApiError, T> {
        delay(100)
        val cookie = cookieStorage.get(Url(ChImpApplication.Companion.NGROK))[0].value
        val session = repoMock.userRepoMock.findSessionByToken(cookie) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(ApiError("User not found"))
        return block(user)
    }

    override suspend fun createChannel(
        name: String,
        creatorId: Int,
        visibility: Visibility,
    ): Either<ApiError, Channel> =
        interceptRequest<Channel> { userCreator ->
            delay(1000)
            if (visibility !in Visibility.entries.toTypedArray()) return@interceptRequest failure(ApiError("Invalid visibility"))
            if (repoMock.channelRepoMock.findChannelByName(name, 1, 0).isNotEmpty()) {
                return@interceptRequest failure(ApiError("Channel name already exists"))
            }
            val channel = repoMock.channelRepoMock.createChannel(name, visibility, userCreator)
            return@interceptRequest success(channel)
        }

    override suspend fun getChannelById(id: Int): Either<ApiError, Channel> =
       interceptRequest<Channel> { user ->
           delay(1000)
           val channel = repoMock.channelRepoMock.findChannelById(id)
               ?: return@interceptRequest failure(ApiError("Channel not found"))
           val userInChannel = repoMock.channelRepoMock.findChannelsOfUser(user)
           if (channel !in userInChannel) {
               return@interceptRequest failure(ApiError("User not in channel"))
           }
           return@interceptRequest success(channel)
       }


    override suspend fun getChannelsOfUser(userId: Int, limit: Int, skip: Int): Either<ApiError, Map<Channel,Role>> =
        interceptRequest<Map<Channel,Role>> { user ->
            if (limit < 0 || skip < 0) return@interceptRequest failure(ApiError("Invalid limit or skip"))
            val userChannels: Map<Channel, Role> = repoMock.channelRepoMock.findChannelsOfUser(user)
            delay(500)
            success(userChannels)
        }

    override suspend fun joinChannel(
        userToAdd: Int,
        channelId: Int,
        role: Role,
    ): Either<ApiError, Channel> =
        interceptRequest<Channel> {
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

    override suspend fun getChannelMembers(channelId: Int): Either<ApiError, List<Pair<User, Role>>> =
        interceptRequest<List<Pair<User, Role>>> { user ->
            delay(1000)
            repoMock.channelRepoMock.findChannelById(channelId) ?: return@interceptRequest failure(ApiError("Channel not found"))
            val userInChannel = repoMock.channelRepoMock.findChannelsOfUser(user)
            if (userInChannel.keys.none { it.id == channelId }) return@interceptRequest failure(ApiError("User not in channel"))
            val members = repoMock.channelRepoMock.getChannelMembers(channelId)
            return@interceptRequest success(members)
        }


    override suspend fun updateChannelName(
        channelId: Int,
        newName: String
    ): Either<ApiError, Channel> =
        interceptRequest<Channel> {
            delay(1000)
            if (newName.isBlank()) return@interceptRequest failure(ApiError("Invalid name"))
            val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: return@interceptRequest failure(ApiError("Channel not found"))
           // repoMock.channelRepoMock.updateChannelName(channelId, newName) TODO
            return@interceptRequest success(channel)
        }

    override suspend fun removeUserFromChannel(
        channelId: Int,
        userID: Int
    ): Either<ApiError, Channel> =
        interceptRequest<Channel>{

            val user = repoMock.userRepoMock.findUserById(userID) ?: return@interceptRequest failure(ApiError("User to remove not found"))
            val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: return@interceptRequest failure(
                ApiError("Channel not found")
            )
            val channelOfUser = repoMock.channelRepoMock.findChannelsOfUser(user)
            if (channel !in channelOfUser) {
                return@interceptRequest failure(ApiError("User already not in channel"))
            }
            repoMock.channelRepoMock.removeUser(userID, channelId)
            return@interceptRequest success(channel)
        }

    override suspend fun searchChannelByName(
        name: String,
        limit: Int,
        skip: Int
    ): Either<ApiError, List<Channel>> = interceptRequest {

        val channel = repoMock.channelRepoMock.findChannelByName(name, limit, skip)
        if (channels.isEmpty()) {
            return@interceptRequest failure(ApiError("Channel not found"))
        }
        return@interceptRequest success(channel)
    }
}