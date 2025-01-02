package pt.isel.chimp.service.http.models.user

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginCredentialsInput(
    val username: String,
    val password: String,
)
