package pt.isel.chimp.repository.interfaces

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User

interface InvitationRepositoryInterface {

    fun getInvitations(receiver: User): Flow<List<Invitation>>
    suspend fun insertInvitations(invitations: List<Invitation>)
    suspend fun deleteInvitation(invitationId: Int)
    suspend fun deleteAllInvitations()
}