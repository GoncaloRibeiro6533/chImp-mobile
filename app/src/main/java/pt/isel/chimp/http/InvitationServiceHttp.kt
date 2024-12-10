package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.http.models.InvitationInputModelChannel
import pt.isel.chimp.http.models.InvitationOutputModelChannel
import pt.isel.chimp.http.models.InvitationsList
import pt.isel.chimp.http.models.channel.ChannelOutputModel
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
    ): Either<ApiError, Invitation> {
        return when (val response = client.post<InvitationOutputModelChannel>(
            url = "/invitation/channel",
            body = InvitationInputModelChannel(senderId, receiverId, channelId, role)
        )) {
            is Success -> success(response.value.toInvitation())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getInvitationsOfUser(): Either<ApiError, List<Invitation>> {
        return when (val response = client.get<InvitationsList>(
            url = "/invitation/user/invitations",
        )) {
            is Success -> success(response.value.invitations.map { it.toInvitation() })
            is Failure -> failure(response.value)
        }
    }

    override suspend fun acceptInvitation(
        invitationId: Int,
    ): Either<ApiError, Channel> {
        return when (val response = client.put<ChannelOutputModel>(
            url = "/invitation/accept/$invitationId",
        )) {
            is Success -> success(response.value.toChannel())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun declineInvitation(
        invitationId: Int,
    ): Either<ApiError, Boolean> {
        return when (val response = client.put<Boolean>(
            url = "/invitation/decline/$invitationId",
        )) {
            is Success -> success(response.value)
            is Failure -> failure(response.value)
        }
    }
}