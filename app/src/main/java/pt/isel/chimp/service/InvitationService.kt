package pt.isel.chimp.service

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.utils.Either

interface InvitationService {

    suspend fun createChannelInvitation(
        senderId: Int,
        receiverId: Int,
        channelId: Int,
        role: Role,
    )
        : Either<ApiError, Invitation>

    suspend fun getInvitationsOfUser(): Either<ApiError, List<Invitation>>

    //todo change return of acceptInv, here and in daw
    suspend fun acceptInvitation(invitationId: Int): Either<ApiError, Channel>

    suspend fun declineInvitation(invitationId: Int): Either<ApiError, Boolean>
}