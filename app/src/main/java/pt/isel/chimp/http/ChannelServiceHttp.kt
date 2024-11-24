package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.http.models.channel.ChannelDTO
import pt.isel.chimp.http.models.channel.ChannelIdsDTO
import pt.isel.chimp.http.models.channel.CreateChannelInputModel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.get
import pt.isel.chimp.http.utils.post
import pt.isel.chimp.http.utils.put
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class ChannelServiceHttp(private val client: HttpClient) : ChannelService {
    override suspend fun createChannel(
        name: String,
        creatorId: Int,
        visibility: Visibility,
        creatorToken: String
    ): Either<ApiError, Channel> {
        return when(val response = client.post<ChannelDTO>(
            url = "/channels",
            token = creatorToken,
            body = CreateChannelInputModel(name, creatorId, visibility)
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun joinChannel(
        token: String,
        userToAdd: Int,
        channelId: Int,
        role: Role
    ): Either<ApiError, Channel> {
        return when(val response = client.put<ChannelDTO>(
            url = "/channels/$channelId/add/$role",
            token = token
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getChannelById(
        id: Int,
        token: String
    ): Either<ApiError, Channel> {
        return when(val response = client.get<ChannelIdsDTO>(
            url = "/channel/$id",
            token = token
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getChannelMembers(
        token: String,
        channelId: Int
    ): Either<ApiError, List<Pair<User, UserInChannel>>> {
        return when(val response = client.get<List<Pair<User, UserInChannel>>>( //todo fix this list type
            url = "/channel/${channelId}/members",
            token = token
        )) {
            is Success -> success(response.value)
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getChannelsOfUser(
        userId: Int,
        limit: Int,
        skip: Int,
        token: String
    ): Either<ApiError, List<Channel>> {
        return when(val response = client.get<List<ChannelDTO>>(
            url = "/channel/user/$userId",
            token = token
        )) {
            is Success -> success(response.value.map { it.toChannel() })
            is Failure -> failure(response.value)
        }
    }

    override suspend fun updateChannelName(
        token: String,
        channelId: Int,
        newName: String
    ): Either<ApiError, Channel> {
        return when(val response = client.put<ChannelDTO>(
            url = "/channel/$channelId/$newName",
            token = token,
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun removeUserFromChannel(
        token: String,
        channelId: Int,
        userID: Int
    ): Either<ApiError, Channel> {
        return when(val response = client.put<ChannelDTO>(
            url = "/channel/$channelId/leave/$userID",
            token = token
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

}