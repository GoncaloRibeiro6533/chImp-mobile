package pt.isel.chimp.service


import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.service.repo.RepoMock
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success
import java.time.LocalDateTime

interface MessageService {
    suspend fun createMessage(senderId: Int, channelId: Int, content: String
    ): Either<MessageError, Message>
    suspend fun getMessagesByChannel(channelId: Int, limit: Int = 10, skip: Int = 0
    ): Either<MessageError, List<Message>>
}

sealed class MessageError(val message: String) {
    data object UserNotFoundException: MessageError("User not found")
    data object ChannelNotFoundException: MessageError("Channel not found")
    data object InvalidSkipOrLimitException : MessageError("Limit and skip must be positive")
}


class MockMessageService(private val repoMock: RepoMock) : MessageService {
        //TODO add token on operations

    override suspend fun createMessage(senderId: Int, channelId: Int, content: String): Either<MessageError, Message> {
        val user = repoMock.userRepoMock.findUserById(senderId) ?: return failure(MessageError.UserNotFoundException)
        val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: return failure(MessageError.ChannelNotFoundException)
        return success(repoMock.messageRepoMock.createMessage(user, channel, content, LocalDateTime.now()))
    }

    override suspend fun getMessagesByChannel(channelId: Int, limit: Int, skip: Int): Either<MessageError, List<Message>> {
        if (limit < 0 || skip < 0) {
            return failure(MessageError.InvalidSkipOrLimitException)
        }
        val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: return failure(MessageError.ChannelNotFoundException)
        return success(repoMock.messageRepoMock.findMessagesByChannel(channel, limit, skip))

    }
}