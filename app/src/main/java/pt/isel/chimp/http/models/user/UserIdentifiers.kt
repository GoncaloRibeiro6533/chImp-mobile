package pt.isel.chimp.http.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserIdentifiers(
    val id: Int,
    val username: String,
)
