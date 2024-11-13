package pt.isel.chimp.domain.invitation

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

data class Invitation(
    val invitationId: Int,
    val sender: User,
    val receiver: User,
    val channel: Channel,
    val role: Role,
    val isUsed: Boolean,
    val timestamp: LocalDateTime,
){
    init {
        require(invitationId >= 0) { "id must be greater than 0" }
        require(role in Role.entries.toTypedArray()) { "Invalid role" }
        require(sender != receiver) { "Sender and receiver must be different" }
        require(timestamp <= LocalDateTime.now()) { "Invalid timestamp" }
    }
}