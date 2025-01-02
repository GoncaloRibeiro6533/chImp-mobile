package pt.isel.chimp.service.service.http

import io.ktor.client.HttpClient
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.service.http.models.MessageHistoryOutputModel
import pt.isel.chimp.service.http.models.MessageInputModel
import pt.isel.chimp.service.http.models.MessageOutputModel
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.service.http.utils.get
import pt.isel.chimp.service.http.utils.post
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class MessageServiceHttp(private val client : HttpClient) : MessageService {

    override suspend fun createMessage(
        channelId: Int,
        content: String
    ): Either<ApiError, Message> {
        return when (val response = client.post<MessageOutputModel>(
            url = "/messages",
            body = MessageInputModel(channelId, content)
        )) {
            is Success -> success(response.value.toMessage())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getMessagesByChannel(
        channelId: Int,
        limit: Int,
        skip: Int
    ): Either<ApiError, List<Message>> {
        return when (val response = client.get<MessageHistoryOutputModel>(
            url = "/messages/history/$channelId?limit=$limit&skip=$skip",
        )) {
            is Success -> success(response.value.messages.map { it.toMessage(response.value.channel.toChannel()) })
            is Failure -> failure(response.value)
        }
    }
}