package pt.isel.chimp.http.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import pt.isel.chimp.http.models.channel.ChannelIdentifiers
import pt.isel.chimp.http.models.user.UserIdentifiers

@Serializable
data class MessageInputModel(
    val channelId: Int,
    val userId: Int,
    val content: String,
)

@Serializable
data class MessageOutputModel(
    val msgId: Int,
    val sender: UserIdentifiers,
    val channel: ChannelIdentifiers,
    val content: String,
    val timestamp: LocalDateTime
)

@Serializable
data class MessageInfoOutputModel(
    val msgId: Int,
    val sender: UserIdentifiers,
    val content: String,
    val timestamp: LocalDateTime
)

@Serializable
data class MessageHistoryOutputModel(
    val nrOfMessages: Int,
    val channel: ChannelIdentifiers,
    val messages: List<MessageInfoOutputModel>,
)
