package pt.isel.chimp.http.models

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.http.models.channel.ChannelOutputModel
import pt.isel.chimp.http.models.user.UserDTO

@Serializable
data class MessageInputModel(
    val channelId: Int,
    val content: String,
)

@Serializable
data class MessageOutputModel(
    val msgId: Int,
    val sender: UserDTO,
    val channel: ChannelOutputModel,
    val content: String,
    val timestamp: LocalDateTime
) {
    fun toMessage() = Message(msgId, sender.toUser(), channel.toChannel(), content, timestamp.toJavaLocalDateTime())
}

@Serializable
data class MessageInfoOutputModel(
    val msgId: Int,
    val sender: UserDTO,
    val content: String,
    val timestamp: LocalDateTime
) {
    fun toMessage(channel: Channel) = Message(msgId, sender.toUser(), channel, content, timestamp.toJavaLocalDateTime())
}

@Serializable
data class MessageHistoryOutputModel(
    val nrOfMessages: Int,
    val channel: ChannelOutputModel,
    val messages: List<MessageInfoOutputModel>,
)
