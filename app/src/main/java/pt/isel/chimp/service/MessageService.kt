package pt.isel.chimp.service


import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.service.repo.RepoMock
import java.time.LocalDateTime

interface MessageService {
    suspend fun createMessage(senderId: Int, channelId: Int, content: String): Message
    suspend fun getMessagesByChannel(channelId: Int, limit: Int = 10, skip: Int = 0): List<Message>
}

class ChannelNotFoundException(message: String, cause: Throwable? = null) : Exception(message, cause)
class InvalidSkipOrLimitException(message: String, cause: Throwable? = null) : Exception(message, cause)


class MockMessageService(private val repoMock: RepoMock) : MessageService {
        //TODO add token on operations

    override suspend fun createMessage(senderId: Int, channelId: Int, content: String): Message {
        val user = repoMock.userRepoMock.findUserById(senderId) ?: throw UserNotFoundException("User not found")
        val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: throw ChannelNotFoundException("Channel not found")
        return repoMock.messageRepoMock.createMessage(user, channel, content, LocalDateTime.now())
    }

    override suspend fun getMessagesByChannel(channelId: Int, limit: Int, skip: Int): List<Message> {
        if (limit < 0 || skip < 0) {
            throw InvalidSkipOrLimitException("Limit and skip must be positive")
        }
        val channel = repoMock.channelRepoMock.findChannelById(channelId) ?: throw ChannelNotFoundException("Channel not found")
        return repoMock.messageRepoMock.findMessagesByChannel(channel, limit, skip)

    }
}