package pt.isel.chimp.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.isel.chimp.domain.user.AuthenticatedUser

interface UserInfoRepository {
    val userInfo: Flow<AuthenticatedUser?>
    suspend fun getUserInfo(): AuthenticatedUser?
    suspend fun updateUserInfo(userInfo: AuthenticatedUser)
    suspend fun clearUserInfo()
}
