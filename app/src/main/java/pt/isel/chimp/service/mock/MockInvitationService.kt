package pt.isel.chimp.service.mock

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Url
import kotlinx.coroutines.delay
import pt.isel.chimp.ChImpApplication
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.service.mock.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class MockInvitationService(
    private val repoMock: RepoMock,
    private val cookieStorage: CookiesStorage
    ) :InvitationService {

    private suspend fun <T: Any>interceptRequest(block: suspend (User) -> Either<ApiError, T>): Either<ApiError, T> {
        delay(100)
        val cookie = cookieStorage.get(Url(ChImpApplication.Companion.NGROK))[0].value
        val session = repoMock.userRepoMock.findSessionByToken(cookie) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(ApiError("User not found"))
        return block(user)
    }


    override suspend fun createChannelInvitation(
        receiverId: Int,
        channelId: Int,
        role: Role,
    ): Either<ApiError, Invitation> =
        interceptRequest<Invitation> { sender ->
            delay(1500)
            val receiver = repoMock.userRepoMock.findUserById(receiverId)
                ?: return@interceptRequest Either.Left(ApiError("Receiver not found"))
            val channel = repoMock.channelRepoMock.findChannelById(channelId)
                ?: return@interceptRequest Either.Left(ApiError("Channel not found"))
            val invitation =
                repoMock.invitationRepoMock.createChannelInvitation(sender, receiver, channel, role)
            return@interceptRequest success(invitation)
        }

    override suspend fun getInvitationsOfUser(): Either<ApiError, List<Invitation>> =
        interceptRequest<List<Invitation>> { user ->
            return@interceptRequest success(
                repoMock.invitationRepoMock.getInvitationsOfUser(user.id)
            )
        }

    override suspend fun acceptInvitation(
        invitationId: Int,
    ): Either<ApiError, Channel> =
        interceptRequest<Channel> { user ->
            val invitation = repoMock.invitationRepoMock.findInvitationById(invitationId)
                ?: return@interceptRequest failure(ApiError("Invitation not found"))
            if (invitation.isUsed) return@interceptRequest failure(ApiError("Invitation already used"))
            val updatedInvitation = repoMock.invitationRepoMock.acceptInvitation(invitationId)
            repoMock.channelRepoMock.addUserToChannel(user, invitation.channel, invitation.role)
            return@interceptRequest success(updatedInvitation.channel)
        }

    override suspend fun declineInvitation(
        invitationId: Int,
    ): Either<ApiError, Boolean> =
        interceptRequest<Boolean> {
            return@interceptRequest success(
                repoMock.invitationRepoMock.declineInvitation(invitationId)
            )
        }

}