package pt.isel.chimp.service.http.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterInput(
    val username: String,
    val email: String,
    val password: String,
)
