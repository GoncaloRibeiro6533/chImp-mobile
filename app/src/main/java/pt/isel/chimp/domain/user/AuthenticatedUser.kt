package pt.isel.chimp.domain.user

data class AuthenticatedUser(
    val user: User,
    val token: String
)
