package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Either

class ChannelServiceHttp(private val client: HttpClient) : ChannelService {
    override suspend fun createChannel(
        creatorToken: String,
        name: String,
        visibility: Visibility
    ): Either<ApiError, Channel> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelById(
        id: Int,
        token: String
    ): Either<ApiError, Channel> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelsByUser(
        token: String,
        limit: Int,
        skip: Int
    ): Either<ApiError, List<Channel>> {
        TODO("Not yet implemented")
    }

    override suspend fun addUserToChannel(
        token: String,
        userToAdd: Int,
        channelId: Int,
        role: Role
    ): Either<ApiError, Channel> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelMembers(
        token: String,
        channel: Channel
    ): Either<ApiError, List<Pair<User, UserInChannel>>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserFromChannel(
        token: String,
        channelId: Int,
        userID: Int
    ): Either<ApiError, Channel> {
        TODO("Not yet implemented")
    }

}