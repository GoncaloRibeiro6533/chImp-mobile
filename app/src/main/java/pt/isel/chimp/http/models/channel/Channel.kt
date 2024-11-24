package pt.isel.chimp.http.models.channel

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.http.models.user.UserDTO


@Serializable
data class CreateChannelInputModel(
    val name: String,
    val creatorId: Int,
    val visibility: Visibility,
)

@Serializable
data class ChannelOutputModel(
    val id: Int,
    val name: String,
    val creator: UserDTO,
    val visibility: Visibility,
) {
    fun toChannel() = Channel(id, name, creator.toUser(), visibility)
}

@Serializable
data class ChannelList(
    val nChannels: Int,
    val channels: List<ChannelOutputModel>,
)

@Serializable
data class ChannelMember(
    val user: UserDTO,
    val role: Role,
)

data class ChannelMembersList(
    val nMembers: Int,
    val members: List<ChannelMember>,
) {
    fun toChannelMembers() = members.map { it.user.toUser() to it.role }
}