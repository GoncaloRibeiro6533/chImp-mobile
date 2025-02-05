package pt.isel.chimp.domain.channel

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.UserParcelable
import pt.isel.chimp.domain.user.User

@Serializable
data class Channel(
    val id: Int,
    val name: String,
    val creator: User,
    val visibility: Visibility,
) {
    init {
        require(id >= 0) { "id must be greater than 0" }
        require(name.isNotBlank()) { "Channel name must not be blank" }
        require(visibility in Visibility.entries.toTypedArray()) { "Invalid visibility" }
    }
    fun toParcelable(role: Role): ChannelParcelable {
        return ChannelParcelable(id, name, creator.toParcelable(), visibility, role)
    }

}