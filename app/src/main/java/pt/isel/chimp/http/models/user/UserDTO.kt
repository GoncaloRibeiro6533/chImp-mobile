package pt.isel.chimp.http.models.user

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.user.User

@Serializable
data class UserIdentifiersDTO(
        val id: Int,
        val username: String,
) {
    fun toUser() = User(id, username, "example@mail.com") //TODO change on api
}

@Serializable
data class UserDTO(
        val id: Int,
        val username: String,
        val email: String,
) {
    fun toUser() = User(id, username, email)
}