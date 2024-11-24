package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.http.models.InvitationInputModelChannel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.get
import pt.isel.chimp.http.utils.post
import pt.isel.chimp.http.utils.put
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class InvitationServiceHttp(private val client: HttpClient) : InvitationService {
    override suspend fun createChannelInvitation(
        senderId: Int,
        receiverId: Int,
        channelId: Int,
        role: Role,
        senderToken: String
    ): Either<ApiError, Invitation> {
        return when (val response = client.post<Invitation>( //TODO change to InvitationDTO?
            url = "/invitation/channel",
            token = senderToken,
            body = InvitationInputModelChannel(senderId, receiverId, channelId, role)
        )) {
            is Success -> success(response.value)
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getInvitationsOfUser(token: String): Either<ApiError, List<Invitation>> {
        return when (val response = client.get<List<Invitation>>(
            url = "/invitation/user/invitations",
            token = token
        )) {
            is Success -> success(response.value)
            is Failure -> failure(response.value)
        }
    }

    override suspend fun acceptInvitation(
        invitationId: Int,
        token: String
    ): Either<ApiError, Channel> {
        return when (val response = client.put<Channel>(
            url = "/invitation/accept/$invitationId",
            token = token,
        )) {
            is Success -> success(response.value)
            is Failure -> failure(response.value)
        }
    }

    override suspend fun declineInvitation(
        invitationId: Int,
        token: String
    ): Either<ApiError, Boolean> {
        return when (val response = client.put<Boolean>(
            url = "/invitation/decline/$invitationId",
            token = token
        )) {
            is Success -> success(response.value)
            is Failure -> failure(response.value)
        }
    }
}