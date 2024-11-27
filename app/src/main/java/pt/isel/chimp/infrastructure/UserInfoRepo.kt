package pt.isel.chimp.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User


class UserInfoRepo(private val store: DataStore<Preferences>) : UserInfoRepository {
    override val userInfo: Flow<AuthenticatedUser?> =
        store.data.map { preferences ->
            preferences.toAuthenticatedUser()
        }

    override suspend fun getUserInfo() :AuthenticatedUser? {
        val preferences = store.data.first()
        return preferences.toAuthenticatedUser()
    }
    override suspend fun updateUserInfo(userInfo: AuthenticatedUser) {
        store.edit { preferences ->
            userInfo.writeToPreferences(preferences)
        }
    }
    override suspend fun clearUserInfo() {
        store.edit { it.clear() }
    }
}

private val USERNAME_KEY = stringPreferencesKey("username")
private val USER_ID_KEY = stringPreferencesKey("userId")
private val TOKEN_KEY = stringPreferencesKey("token")
private val USER_EMAIL_KEY = stringPreferencesKey("email")


private fun Preferences.toAuthenticatedUser(): AuthenticatedUser? {
    val username = this[USERNAME_KEY] ?: return null
    val token = this[TOKEN_KEY] ?: return null
    val userId = this[USER_ID_KEY] ?: return null
    val email = this[USER_EMAIL_KEY] ?: return null
    return AuthenticatedUser(User(userId.toInt(), username, email), token)
}

private fun AuthenticatedUser.writeToPreferences(preferences: MutablePreferences): MutablePreferences {
    preferences[USERNAME_KEY] = user.username
    preferences[TOKEN_KEY] = token
    preferences[USER_ID_KEY] = user.id.toString()
    preferences[USER_EMAIL_KEY] = user.email
    return preferences
}
