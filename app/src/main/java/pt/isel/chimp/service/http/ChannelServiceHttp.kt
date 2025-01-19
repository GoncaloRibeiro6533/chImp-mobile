package pt.isel.chimp.service.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.http.models.channel.ChannelList
import pt.isel.chimp.service.http.models.channel.ChannelMembersList
import pt.isel.chimp.service.http.models.channel.ChannelOfUserList
import pt.isel.chimp.service.http.models.channel.ChannelOutputModel
import pt.isel.chimp.service.http.models.channel.CreateChannelInputModel
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.service.http.utils.get
import pt.isel.chimp.service.http.utils.post
import pt.isel.chimp.service.http.utils.put
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
    ): Either<ApiError, Channel> {
        return when(val response = client.post<ChannelOutputModel>(
            url = "/channels",
            body = CreateChannelInputModel(name, creatorId, visibility)
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun joinChannel(
        userToAdd: Int,
        channelId: Int,
        role: Role
    ): Either<ApiError, Channel> {
        return when(val response = client.put<ChannelOutputModel>(
            url = "/channels/$channelId/add/$role",
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getChannelById(
        id: Int,
    ): Either<ApiError, Channel> {
        return when(val response = client.get<ChannelOutputModel>(
            url = "/channels/$id",
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getChannelMembers(
        channelId: Int
    ): Either<ApiError, List<Pair<User, Role>>> {
        return when(val response = client.get<ChannelMembersList>(
            url = "/channels/${channelId}/members",
        )) {
            is Success -> success(response.value.toChannelMembers())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getChannelsOfUser(
        userId: Int,
        limit: Int,
        skip: Int
    ): Either<ApiError, Map<Channel,Role>> {
        return when(val response = client.get<ChannelOfUserList>(
            url = "/channels/user/$userId",
        )) {
            is Success -> success(response.value.toChannelOfUser())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun updateChannelName(
        channelId: Int,
        newName: String
    ): Either<ApiError, Channel> {
        return when(val response = client.put<ChannelOutputModel>(
            url = "/channels/$channelId/$newName",
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun removeUserFromChannel(
        channelId: Int,
        userID: Int
    ): Either<ApiError, Channel> {
        return when(val response = client.put<ChannelOutputModel>(
            url = "/channels/$channelId/leave",
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun searchChannelByName(
        name: String,
        limit: Int,
        skip: Int
    ): Either<ApiError, List<Channel>> {
        return when(val response = client.get<ChannelList>(
            url = "/channels/search/$name",
        )) {
            is Success -> success(response.value.toChannelList())
            is Failure -> failure(response.value)
        }
    }

}