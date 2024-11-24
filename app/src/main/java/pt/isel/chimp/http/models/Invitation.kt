package pt.isel.chimp.http.models


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.http.models.channel.ChannelOutputModel
import pt.isel.chimp.http.models.user.UserIdentifiers


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
    val sender: UserIdentifiers,
    val receiver: UserIdentifiers,
    val channel: ChannelOutputModel,
    val role: Role,
    val timestamp: LocalDateTime,
)

@Serializable
data class InvitationInputModelRegister(
    val email: String,
    val channelId: Int,
    val role: Role,
)

@Serializable
data class InvitationOutputModelRegister(
    val id: Int,
    val sender: UserIdentifiers,
    val email: String,
    val channel: ChannelOutputModel,
    val role: Role,
    val timestamp: LocalDateTime,
)

@Serializable
data class InvitationsList(
    val nInvitations: Int,
    val invitations: List<InvitationOutputModelChannel>,
)
