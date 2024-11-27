package pt.isel.chimp.http

import io.ktor.client.HttpClient
import io.ktor.http.parameters
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.http.models.MessageHistoryOutputModel
import pt.isel.chimp.http.models.MessageInputModel
import pt.isel.chimp.http.models.MessageOutputModel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.post
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

class MessageServiceHttp(private val client : HttpClient) : MessageService {
    override suspend fun createMessage(
        token: String,
        channelId: Int,
        content: String
    ): Either<ApiError, Message> {
        return when (val response = client.post<MessageOutputModel>(
            url = "/messages",
            token = token,
            body = MessageInputModel(channelId, content)
        )) {
            is Success -> success(response.value.toMessage())
            is Failure -> failure(response.value)
        }
    }

    override suspend fun getMessagesByChannel(
        token: String,
        channelId: Int,
        limit: Int,
        skip: Int
    ): Either<ApiError, List<Message>> {
        return when (val response = client.post<MessageHistoryOutputModel>(
            url = "/messages/history/$channelId",
            token = token,
            parameters { append("limit", limit.toString())
                          append("skip", skip.toString()) }
        )) {
            is Success -> success(response.value.messages.map { it.toMessage(response.value.channel.toChannel()) })
            is Failure -> failure(response.value)
        }
    }
}