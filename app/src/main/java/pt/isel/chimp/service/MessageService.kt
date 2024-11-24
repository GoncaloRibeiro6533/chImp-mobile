package pt.isel.chimp.service


import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.utils.Either


/**
 * Contract of the service that provides the messages of a channel.
 */

interface MessageService {

    /**
     * Creates a message.
     * @param token the user token.
     * @param senderId the id of the sender.
     * @param channelId the id of the channel.
     * @param content the message content.
     * @return the created message.
     * @return ApiError if an error occurs.
     */
    suspend fun createMessage(token: String, senderId: Int, channelId: Int, content: String
    ): Either<ApiError, Message>


    /**
     * Gets the messages of a channel.
     * @param token the user token.
     * @param channelId the id of the channel.
     * @param limit the maximum number of messages to return.
     * @param skip the number of messages to skip.
     * @return the messages.
     * @return ApiError if an error occurs.
     */
    suspend fun getMessagesByChannel(token: String, channelId: Int, limit: Int = 10, skip: Int = 0
    ): Either<ApiError, List<Message>>

    //TODO add getMsgHistory?
}




