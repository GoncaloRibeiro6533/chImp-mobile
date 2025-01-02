package pt.isel.chimp.service.http.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UsernameUpdateInput(
    val newUsername: String,
)
