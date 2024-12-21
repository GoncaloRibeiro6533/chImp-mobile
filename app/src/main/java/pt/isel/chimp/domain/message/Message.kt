package pt.isel.chimp.domain.message

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime
//TODO maybe a property sended or sending, or a list in a repository
@Serializable
data class Message(
    val id: Int,
    val sender: User,
    val channel: Channel,
    val content: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime,
) {
    companion object {
        const val MAX_MESSAGE_LENGTH = 1000
    }

    init {
        require(id >= 0) { "Id must be greater than 0" }
        require(content.isNotBlank()) { "A message can't be blank" }
        require(content.length <= MAX_MESSAGE_LENGTH) {
            "Content must be less than $MAX_MESSAGE_LENGTH characters"
        require(timestamp <= LocalDateTime.now()) { "Invalid timestamp" }
        }
    }
}