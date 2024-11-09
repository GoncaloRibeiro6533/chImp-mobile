package pt.isel.chimp.http.models.user

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginCredentialsInput(
    val username: String,
    val password: String,
)
