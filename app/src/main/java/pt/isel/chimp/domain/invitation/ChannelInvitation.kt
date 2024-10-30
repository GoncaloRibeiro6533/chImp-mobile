package pt.isel.chimp.domain.invitation

import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

class ChannelInvitation(
    id: Int,
    sender: User,
    val receiver: User,
    val channel: Channel,
    val role: Role,
    isUsed: Boolean = false,
    timestamp: LocalDateTime,
) : Invitation(id, sender, isUsed, timestamp) {
    init {
        require(id >= 0) { "id must be greater than 0" }
        require(role in Role.entries.toTypedArray()) { "Invalid role" }
        require(sender != receiver) { "Sender and receiver must be different" }
        require(timestamp.isAfter(LocalDateTime.now())) { "Invalid timestamp" }
    }

    private fun copy() = ChannelInvitation(id, sender, receiver, channel, role, true, timestamp)

    override fun markAsUsed() = copy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChannelInvitation) return false

        if (id != other.id) return false
        if (sender != other.sender) return false
        if (receiver != other.receiver) return false
        if (channel != other.channel) return false
        if (role != other.role) return false
        if (isUsed != other.isUsed) return false
        if (timestamp != other.timestamp) return false

        return true
    }
}
