package pt.isel.chimp.domain.channel

import pt.isel.chimp.domain.user.User

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
}