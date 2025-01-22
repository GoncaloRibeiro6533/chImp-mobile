package pt.isel.chimp.workManager.sse

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User


@Serializable
data class NewChannelMessage(
    val id: Long, // SSE Event identifier
    val message: Message, // New channel message
)

@Serializable
data class ChannelNameUpdate(
    val id: Long, // SSE Event identifier
    val channel: Channel, // New channel
)

@Serializable
data class ChannelNewMemberUpdate(
    val id: Long, // SSE Event identifier
    val newMember: NewMember,
)

@Serializable
data class ChannelMemberExitedUpdate(
    val id: Long, // SSE Event identifier
    val removedMember: RemovedMember,
)

@Serializable
data class NewInvitationUpdate(
    val id: Long, // SSE Event identifier
    val invitation: Invitation, // New invitation
)
@Serializable
data class InvitationAcceptedUpdate(
    val id: Long, // SSE Event identifier
    val invitation: Invitation, // Accepted invitation
)

@Serializable
data class NewMember(
    val channel: Channel, // Channel with updated members
    val newMember: User,
    val role : Role
)

@Serializable
data class RemovedMember(
    val channel: Channel, // Channel with updated members
    val removedMember: User
)

@Serializable
data class MemberUsernameUpdate(
    val id: Long,
    val updatedMember: User
)
