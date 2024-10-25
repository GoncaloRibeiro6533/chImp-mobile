package pt.isel.chimp.domain

data class AuthenticatedUser(
    val id: Int,
    val token: String
)
