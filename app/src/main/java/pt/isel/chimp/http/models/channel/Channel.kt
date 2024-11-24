package pt.isel.chimp.http.models.channel

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.http.models.user.UserIdentifiers


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
    val creator: UserIdentifiers,
    val visibility: Visibility,
)

@Serializable
data class ChannelList(
    val nChannels: Int,
    val channels: List<ChannelOutputModel>,
)

@Serializable
data class ChannelIdentifiers(
    val id: Int,
    val name: String,
)
