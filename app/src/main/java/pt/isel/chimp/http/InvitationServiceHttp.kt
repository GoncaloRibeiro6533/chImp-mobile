package pt.isel.chimp.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.utils.Either

class InvitationServiceHttp(private val client: HttpClient) : InvitationService {
    override suspend fun createChannelInvitation(
        senderToken: String,
        receiverId: Int,
        channelId: Int,
        role: Role
    ): Either<ApiError, Invitation> {
        TODO("Not yet implemented")
    }

    override suspend fun getInvitationsOfUser(token: String): Either<ApiError, List<Invitation>> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptInvitation(
        invitationId: Int,
        token: String
    ): Either<ApiError, Channel> {
        TODO("Not yet implemented")
    }

    override suspend fun declineInvitation(
        invitationId: Int,
        userId: Int,
        token: String
    ): Either<ApiError, Boolean> {
        TODO("Not yet implemented")
    }
}