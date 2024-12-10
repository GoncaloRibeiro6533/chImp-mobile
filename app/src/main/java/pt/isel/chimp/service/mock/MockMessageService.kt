package pt.isel.chimp.service.mock

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Url
import kotlinx.coroutines.delay
import pt.isel.chimp.ChImpApplication
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success
import java.time.LocalDateTime

class MockMessageService(
    private val repoMock: RepoMock,
    private val cookieStorage: CookiesStorage
    ) : MessageService {

    private suspend fun <T: Any>interceptRequest(block: suspend (User) -> Either<ApiError, T>): Either<ApiError, T> {
        delay(100)
        val cookie = cookieStorage.get(Url(ChImpApplication.Companion.NGROK))[0].value
        val session = repoMock.userRepoMock.findSessionByToken(cookie) ?: return failure(ApiError("Unauthorized"))
        val user = repoMock.userRepoMock.findUserById(session.userId) ?: return failure(ApiError("User not found"))
        return block(user)
    }

    override suspend fun createMessage(
        channelId: Int,
        content: String
    ): Either<ApiError, Message> =
        interceptRequest<Message> { user ->
            delay(1000)
            val channel = repoMock.channelRepoMock.findChannelById(channelId) ?:
            return@interceptRequest failure(ApiError("Channel not found"))
            return@interceptRequest success(
                repoMock.messageRepoMock.createMessage(user, channel, content, LocalDateTime.now()))
        }

    override suspend fun getMessagesByChannel(
        channelId: Int,
        limit: Int,
        skip: Int)
    : Either<ApiError, List<Message>> =
        interceptRequest<List<Message>> { user ->
            delay(1000)
            if (limit < 0 || skip < 0) return@interceptRequest failure(ApiError("Invalid limit or skip"))
            val channel = repoMock.channelRepoMock.findChannelById(channelId) ?:
            return@interceptRequest failure(ApiError("Channel not found"))
            return@interceptRequest success(
                repoMock.messageRepoMock.findMessagesByChannel(channel, limit, skip)
            )
        }
}