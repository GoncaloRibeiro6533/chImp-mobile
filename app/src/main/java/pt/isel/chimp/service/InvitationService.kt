package pt.isel.chimp.service

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.utils.Either

interface InvitationService {

    /**
     * Creates a new invitation.
     * @param receiverId the id of the receiver user.
     * @param channelId the id of the channel to invite.
     * @param role attributed role to the receiver user if accepted.
     * @return the created invitation.
     * @return ApiError if an error occurs.
     */
    suspend fun createChannelInvitation(
        receiverId: Int,
        channelId: Int,
        role: Role,
    )
        : Either<ApiError, Invitation>

    /**
     * Gets all the invitations sent to a user.
     * @return the list with all invitations.
     * @return ApiError if an error occurs.
     */
    suspend fun getInvitationsOfUser(): Either<ApiError, List<Invitation>>

    /**
     * Accepts an invitation to join a channel.
     * @param invitationId the invitation Id.
     * @return the joined channel.
     * @return ApiError if an error occurs.
     */
    suspend fun acceptInvitation(invitationId: Int): Either<ApiError, Invitation>

    /**
     * Declines an invitation to join a channel.
     * @param invitationId the invitation Id.
     * @return check.
     * @return ApiError if an error occurs.
     */
    suspend fun declineInvitation(invitationId: Int): Either<ApiError, Boolean>
}