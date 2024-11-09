package pt.isel.chimp.http.models.user

import kotlinx.serialization.Serializable
import pt.isel.chimp.domain.user.AuthenticatedUser

@Serializable
data class AuthenticatedUserDTO(
    val user: UserDTO,
    val token: String
) {
    fun toAuthenticatedUser() = AuthenticatedUser(user.toUser(), token)
}
