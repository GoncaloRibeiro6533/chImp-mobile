package pt.isel.chimp.service.mock.repo

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

class InvitationRepoMock {

    companion object{
        private val invitations =
            mutableListOf<Invitation>(
                Invitation(1, UserRepoMock.Companion.users[1], UserRepoMock.Companion.users[0],
                    ChannelRepoMock.Companion.channels[3],
                    Role.READ_WRITE, false, LocalDateTime.now()),
                Invitation(2, UserRepoMock.Companion.users[0], UserRepoMock.Companion.users[1], ChannelRepoMock.Companion.channels[2],
                    Role.READ_ONLY,false, LocalDateTime.now()),
            )
        private var currentInvId = 50
    }

    fun createChannelInvitation(sender: User, receiver: User, channel: Channel, role: Role) :Invitation {
        val invitation = Invitation(currentInvId++, sender, receiver, channel, role, false, LocalDateTime.now())
        invitations.add(invitation)
        return invitation
    }

    fun getInvitationsOfUser(userId: Int): List<Invitation> {
        return invitations.filter { it.receiver.id == userId }
    }

    fun acceptInvitation(invitationId: Int): Invitation {
        val invitation = invitations.first { it.id == invitationId}
        val newInv = invitation.copy(isUsed = true)
        invitations.remove(invitation)
        invitations.add(newInv)
        return invitation
    }

    fun declineInvitation(invitationId: Int): Boolean {
        val invitation = invitations.first { it.id == invitationId}
        invitations.remove(invitation)
        return true
    }

    fun findInvitationById(invitationId: Int): Invitation? {
        return invitations.find { it.id == invitationId }
    }

    //todo implement findInvitationByChannelId here?
}