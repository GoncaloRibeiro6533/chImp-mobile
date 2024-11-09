package pt.isel.chimp.http.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterInput(
    val username: String,
    val email: String,
    val password: String,
)
