package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.utils.Either

interface InvitationRepositoryInterface {

    suspend fun fetchInvitations(receiver: User): Flow<List<Invitation>>
    suspend fun hasInvitations(): Boolean
    suspend fun insertInvitations(invitations: List<Invitation>)
    suspend fun deleteInvitation(invitationId: Int)
    suspend fun deleteAllInvitations()
    suspend fun acceptInvitation(invitation: Invitation): Either<ApiError,Channel>
    suspend fun declineInvitation(invitation: Invitation): Either<ApiError, Unit>
    suspend fun createInvitation(receiverId: Int, channelId: Int, role: Role): Either<ApiError, Unit>
}