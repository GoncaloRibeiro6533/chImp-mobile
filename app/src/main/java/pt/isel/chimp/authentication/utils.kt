package pt.isel.chimp.authentication

import pt.isel.chimp.domain.AuthenticatedUser


const val MIN_USERNAME_LENGTH = 3
const val MAX_USERNAME_LENGTH = 40

const val MIN_PASSWORD_LENGTH = 4
const val MAX_PASSWORD_LENGTH = 127

val DEFAULT_LOGIN_RESPONSE = AuthenticatedUser(0, "")

const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"
