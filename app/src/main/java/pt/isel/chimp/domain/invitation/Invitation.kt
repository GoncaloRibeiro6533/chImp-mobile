package pt.isel.chimp.domain.invitation

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.LocalDateTimeSerializer
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

/**
 * Represents an invitation to a channel.
 * @property id the invitation id.
 * @property sender the user that sent the invitation.
 * @property receiver the user that received the invitation.
 * @property channel the channel to which the invitation is for.
 * @property role the role that the receiver will have in the channel.
 * @property isUsed whether the invitation has been used.
 * @property timestamp the timestamp of the invitation.
 */
@Serializable
data class Invitation(
    val id: Int,
    val sender: User,
    val receiver: User,
    val channel: Channel,
    val role: Role,
    val isUsed: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime,
){
    init {
        require(id >= 0) { "id must be greater than 0" }
        require(role in Role.entries.toTypedArray()) { "Invalid role" }
        require(sender != receiver) { "Sender and receiver must be different" }
    }
}