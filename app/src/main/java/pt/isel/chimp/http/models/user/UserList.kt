package pt.isel.chimp.http.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserList(
    val users: List<UserDTO>,
    val nusers: Int,
)
