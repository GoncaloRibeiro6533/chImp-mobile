package pt.isel.chimp.ui.screens.authentication

/**
 * Validates the username.
 *
 * @param username username
 * @return true if the username is valid, false otherwise
 */
fun validateUsername(username: String): Boolean =
    username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH

/**
 * Validates the email.
 *
 * @param email the email to validate
 * @return true if the email is valid, false otherwise
 */
fun validateEmail(email: String): Boolean = email.matches(EMAIL_REGEX.toRegex())

/**
 * Validates the password.
 *
 * @param password password
 * @return true if the password is valid, false otherwise
 */
fun validatePassword(password: String): Boolean =
    password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH
