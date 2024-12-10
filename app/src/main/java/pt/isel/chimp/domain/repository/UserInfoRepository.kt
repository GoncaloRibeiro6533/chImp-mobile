package pt.isel.chimp.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.user.User

interface UserInfoRepository {
    val userInfo: Flow<User?>
    suspend fun getUserInfo(): User?
    suspend fun updateUserInfo(userInfo: User)
    suspend fun clearUserInfo()
}
