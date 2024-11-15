package pt.isel.chimp.service.repo

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.repo.ChannelRepoMock.Companion.channels
import pt.isel.chimp.service.repo.UserRepoMock.Companion.users

class InvitationRepoMock {

    companion object{
        private val invitations =
            mutableListOf<Invitation>(
                Invitation(1, users[1], users[0],channels[1],
                    Role.READ_WRITE, false, java.time.LocalDateTime.now()),
                Invitation(2, users[0], users[1], channels[2],
                    Role.READ_ONLY,false, java.time.LocalDateTime.now()),
            )
        private var currentInvId = 50
    }

    fun createChannelInvitation(sender: User, receiver: User, channel: Channel, role: Role) :Invitation {
        val invitation = Invitation(currentInvId++, sender, receiver, channel, role, false, java.time.LocalDateTime.now())
        invitations.add(invitation)
        return invitation
    }

    fun getInvitationsOfUser(userId: Int): List<Invitation> {
        return invitations.filter { it.receiver.id == userId }
    }

    fun acceptInvitation(invitationId: Int): Channel {
        val invitation = invitations.first { it.invitationId == invitationId}
        val newInv = invitation.copy(isUsed = true)
        invitations.remove(invitation)
        invitations.add(newInv)
        return invitation.channel
    }

    fun declineInvitation(invitationId: Int): Boolean {
        val invitation = invitations.first { it.invitationId == invitationId}
        invitations.remove(invitation)
        return true
    }

    fun findInvitationById(invitationId: Int): Invitation? {
        return invitations.find { it.invitationId == invitationId }
    }

    //todo implement findInvitationByChannelId here?
}