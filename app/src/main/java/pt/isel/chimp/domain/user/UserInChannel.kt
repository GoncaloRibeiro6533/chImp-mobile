package pt.isel.chimp.domain.user

import pt.isel.chimp.domain.Role

data class UserInChannel(val userId: Int, val channelId: Int, val role: Role)
