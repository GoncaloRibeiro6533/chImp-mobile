package pt.isel.chimp.domain.invitation

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

class RegisterInvitation(
    id: Int,
    sender: User,
    val email: String,
    val channel: Channel,
    val role: Role,
    isUsed: Boolean = false,
    timestamp: LocalDateTime,
) : Invitation(id, sender, isUsed, timestamp) {
    init {
        require(id >= 0) { "id must be greater than 0" }
        require(email.isNotBlank()) { "Email must not be blank" }
        require(sender.email != email) { "Sender and receiver email must be different" }
        //TODO require(timestamp <= LocalDateTime.now()) { "Invalid timestamp" }
    }

    fun copy() = RegisterInvitation(id, sender, email, channel, role, true, timestamp)

    override fun markAsUsed() = copy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RegisterInvitation) return false

        if (id != other.id) return false
        if (sender != other.sender) return false
        if (email != other.email) return false
        if (channel != other.channel) return false
        if (role != other.role) return false
        if (isUsed != other.isUsed) return false
        if (timestamp != other.timestamp) return false

        return true
    }
}
