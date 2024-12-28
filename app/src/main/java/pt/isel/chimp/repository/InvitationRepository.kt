package pt.isel.chimp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.InvitationEntity
//import pt.isel.chimp.storage.entities.InvitationEntity
import pt.isel.chimp.storage.entities.UserEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

class InvitationRepository(
    private val db: ChImpClientDB
) {

    suspend fun insertInvitations(invitations: List<Invitation>) {
        val channels = invitations.map { it.channel }.distinct()
        val senders = invitations.map { it.sender }.distinct()
        val creators = channels.map { it.creator }.distinct()
        val users = (senders + creators).distinct()
        val invitationsEntity = invitations.map {
            InvitationEntity(
                it.id,
                it.sender.id,
                it.receiver.id,
                it.channel.id,
                it.role.name,
                it.isUsed,
                it.timestamp.toEpochSecond(ZoneOffset.UTC)
            )
        }
        db.userDao().insertUsers(*users.map {
            UserEntity(
                it.id,
                it.username,
                it.email
            )
        }.toTypedArray())
        db.channelDao().insertChannels(*channels.map {
            ChannelEntity(
                it.id,
                it.name,
                it.creator.id,
                it.visibility.name,
                invitations.first { invitation -> invitation.channel.id == it.id }.role.name
            )
        }.toTypedArray())
        db.invitationDao().insertInvitations(*invitationsEntity.toTypedArray())
    }


    fun getInvitations(receiver: User) : Flow<List<Invitation>> {
        return db.invitationDao().getAllInvitations().map { list ->
            list.map { it ->
                Invitation(
                    it.invitation.invitationId,
                    User(it.sender.id, it.sender.username, it.sender.email),
                    receiver,
                    Channel(it.channel.id, it.channel.name, User(it.creator.id, it.creator.username, it.creator.email),
                        Visibility.valueOf(it.channel.visibility)),
                    Role.valueOf(it.invitation.roleI),
                    it.invitation.isUsed,
                    LocalDateTime.ofEpochSecond(it.invitation.timestamp, 0, ZoneOffset.UTC)
                )
            }
        }
    }

    suspend fun deleteInvitation(invitationId: Int) {
        db.invitationDao().deleteInvitation(invitationId)
    }

    suspend fun deleteAllInvitations() {
        db.invitationDao().deleteAllInvitations()
    }
}