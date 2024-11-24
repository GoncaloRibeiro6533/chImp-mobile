package pt.isel.chimp.service.mock

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.service.repo.ChannelRepoMock
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class MockInvitationService(private val repoMock: RepoMock) :InvitationService {

    private suspend fun <T: Any>interceptRequest(
        token: String,
        block: suspend (User) -> Either<ApiError, T>
    ): Either<ApiError, T> {
        val session = repoMock.userRepoMock.findSessionByToken(token)
            ?: return Either.Left(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId)
            ?: return failure(ApiError("User not found"))
        return block(user)
    }

    override suspend fun createChannelInvitation(
        senderId: Int, //todo senderId adicionado por causa do http
        receiverId: Int,
        channelId: Int,
        role: Role,
        senderToken: String
    ): Either<ApiError, Invitation> =
        interceptRequest<Invitation>(senderToken) { sender ->
            val receiver = repoMock.userRepoMock.findUserById(receiverId)
                ?: return@interceptRequest Either.Left(ApiError("Receiver not found"))
            val channel = repoMock.channelRepoMock.findChannelById(channelId)
                ?: return@interceptRequest Either.Left(ApiError("Channel not found"))
            val invitation =
                repoMock.invitationRepoMock.createChannelInvitation(sender, receiver, channel, role)
            return@interceptRequest success(invitation)
        }

    override suspend fun getInvitationsOfUser(token: String): Either<ApiError, List<Invitation>> =
        interceptRequest<List<Invitation>>(token) { user ->
            return@interceptRequest success(
                repoMock.invitationRepoMock.getInvitationsOfUser(user.id)
            )
        }

    override suspend fun acceptInvitation(
        invitationId: Int,
        token: String
    ): Either<ApiError, Channel> =
        interceptRequest<Channel>(token) { user ->
            val invitation = repoMock.invitationRepoMock.findInvitationById(invitationId)
                ?: return@interceptRequest failure(ApiError("Invitation not found"))
            if (invitation.isUsed) return@interceptRequest failure(ApiError("Invitation already used"))
            val channel = repoMock.invitationRepoMock.acceptInvitation(invitationId)
            ChannelRepoMock.userInChannel.add(UserInChannel(user.id, channel.id, role = invitation.role))
            return@interceptRequest success(channel)
        }

    override suspend fun declineInvitation(
        invitationId: Int,
        token: String
    ): Either<ApiError, Boolean> =
        interceptRequest<Boolean>(token) {
            return@interceptRequest success(
                repoMock.invitationRepoMock.declineInvitation(invitationId)
            )
        }

}