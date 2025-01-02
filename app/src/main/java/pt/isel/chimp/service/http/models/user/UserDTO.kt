package pt.isel.chimp.service.http.models.user

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.user.User



@Serializable
data class UserDTO(
        val id: Int,
        val username: String,
        val email: String,
) {
    fun toUser() = User(id, username, email)
}