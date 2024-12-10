package pt.isel.chimp.domain.repository

import kotlinx.coroutines.flow.Flow


interface CookiesRepository {
    val cookies: Flow<Map<String, String>>
    fun setCookie(key: String, value: String)
    fun getCookie(key: String): String?
    fun removeCookie(key: String)
    fun clearCookies()
}