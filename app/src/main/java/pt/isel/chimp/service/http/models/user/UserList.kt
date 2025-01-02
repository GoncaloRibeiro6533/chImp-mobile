package pt.isel.chimp.service.http.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserList(
    val users: List<UserDTO>,
    val nusers: Int,
)
