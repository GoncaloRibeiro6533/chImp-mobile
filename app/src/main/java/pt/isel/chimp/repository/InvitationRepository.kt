package pt.isel.chimp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.repository.interfaces.InvitationRepositoryInterface
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.http.utils.ChImpException
import pt.isel.chimp.storage.ChImpClientDB
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.InvitationEntity
import pt.isel.chimp.storage.entities.UserEntity
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success
import java.time.LocalDateTime
import java.time.ZoneOffset

class InvitationRepository(
    private val local: ChImpClientDB,
    private val remote: ChImpService
): InvitationRepositoryInterface
{

    override suspend fun insertInvitations(invitations: List<Invitation>) {
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
        local.userDao().insertUsers(*users.map {
            UserEntity(
                it.id,
                it.username,
                it.email
            )
        }.toTypedArray())
        local.channelDao().insertChannels(*channels.map {
            ChannelEntity(
                it.id,
                it.name,
                it.creator.id,
                it.visibility.name,
                invitations.first { invitation -> invitation.channel.id == it.id }.role.name
            )
        }.toTypedArray())
        local.invitationDao().insertInvitations(*invitationsEntity.toTypedArray())
    }

    override suspend fun hasInvitations(): Boolean {
        return local.invitationDao().hasInvitations() > 0
    }

    private fun getInvitations(receiver: User) : Flow<List<Invitation>> {
        return local.invitationDao().getAllInvitations().map { list ->
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

    override suspend fun fetchInvitations(receiver: User): Flow<List<Invitation>> {
        if (!hasInvitations()) {
            when(val invitations = remote.invitationService.getInvitationsOfUser()) {
                is Success -> {
                    insertInvitations(invitations.value)
                }
                is Failure -> {
                    throw ChImpException(invitations.value.message, null)
                }
            }
        }
        return getInvitations(receiver)
    }

    override suspend fun acceptInvitation(invitation: Invitation): Either<ApiError,Channel> =
        when(val result= remote.invitationService.acceptInvitation(invitation.id)){
            is Success -> {
                local.invitationDao().deleteInvitation(invitation.id)
                success(result.value)
            }
            is Failure -> {
                if (result.value == ApiError("Invitation expired")) {
                    local.invitationDao().deleteInvitation(invitation.id)
                }
                failure(result.value)
            }
        }

    override suspend fun declineInvitation(invitation: Invitation): Either<ApiError, Unit> =
        when(val result= remote.invitationService.declineInvitation(invitation.id)){
            is Success -> {
                local.invitationDao().deleteInvitation(invitation.id)
                success(Unit)
            }
            is Failure -> {
                if (result.value == ApiError("Invitation expired")) {
                    local.invitationDao().deleteInvitation(invitation.id)
                }
                failure(result.value)
            }
        }

    override suspend fun deleteInvitation(invitationId: Int) {
        local.invitationDao().deleteInvitation(invitationId)
    }

    override suspend fun deleteAllInvitations() {
        local.invitationDao().deleteAllInvitations()
    }

    override suspend fun createInvitation(receiverId: Int, channelId: Int, role: Role): Either<ApiError, Unit> =
        when (val result = remote.invitationService.createChannelInvitation(receiverId, channelId, role)) {
            is Success -> {
                success(Unit)
            }
            is Failure -> {
                failure(result.value)
            }
        }

}