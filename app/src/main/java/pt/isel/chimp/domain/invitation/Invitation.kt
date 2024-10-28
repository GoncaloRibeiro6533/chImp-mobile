package pt.isel.chimp.domain.invitation

import pt.isel.chimp.domain.user.User
import java.time.LocalDateTime

sealed class Invitation(
    val id: Int,
    val sender: User,
    val isUsed: Boolean,
    val timestamp: LocalDateTime,
) {
    abstract fun markAsUsed(): Invitation
}