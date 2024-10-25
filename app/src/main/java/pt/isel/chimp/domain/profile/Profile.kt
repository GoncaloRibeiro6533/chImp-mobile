package pt.isel.chimp.domain.profile

/**
 * Represents a user profile.
 * @property username the username of the user.
 * @property email the email of the user.
 */


data class Profile (
    val username: String,
    val email: String,
) {
    init {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(email.isNotBlank()) { "Email cannot be blank" }
    }
}