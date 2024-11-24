package pt.isel.chimp.http.models


import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.http.models.channel.ChannelOutputModel
import pt.isel.chimp.http.models.user.UserDTO


@Serializable
data class InvitationInputModelChannel(
    val senderId: Int,
    val receiverId: Int,
    val channelId: Int,
    val role: Role,
)

@Serializable
data class InvitationOutputModelChannel(
    val id: Int,
    val sender: UserDTO,
    val receiver: UserDTO,
    val channel: ChannelOutputModel,
    val role: Role,
    val timestamp: LocalDateTime,
) {
    fun toInvitation() = Invitation(
        id,
        sender.toUser(),
        receiver.toUser(),
        channel.toChannel(),
        role,
        isUsed = false,
        timestamp = timestamp.toJavaLocalDateTime()
    )
}


@Serializable
data class InvitationsList(
    val nInvitations: Int,
    val invitations: List<InvitationOutputModelChannel>,
)
