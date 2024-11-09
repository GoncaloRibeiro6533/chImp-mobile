package pt.isel.chimp.http.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UsernameUpdateInput(
    val newUsername: String,
)
