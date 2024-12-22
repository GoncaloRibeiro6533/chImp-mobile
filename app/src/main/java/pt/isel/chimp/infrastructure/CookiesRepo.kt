package pt.isel.chimp.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CookiesRepo(
    private val store: DataStore<Preferences>,
) : CookiesStorage {

    val cookies: Flow<List<Cookie>> = store.data.map { preferences ->
        preferences.toCookies()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        store.edit { preferences ->
            val existingCookies = preferences.toCookies().toMutableList()
            existingCookies.removeIf {
                it.name == cookie.name && it.domain == cookie.domain && it.path == cookie.path
            }
            //TODO
            val c = cookie.copy(
                domain = requestUrl.host,
            )
            existingCookies.add(c)
            preferences.writeCookies(existingCookies)
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val preferences = store.data.first()
        return preferences.toCookies().filter { cookie ->
            cookie.matches(requestUrl)
        }
    }

    suspend fun getAllCookies(): List<Cookie> {
        return store.data.first().toCookies()
    }

    override fun close() {}


}

    private val COOKIE_KEY = stringPreferencesKey("cookie")

    private fun Preferences.toCookies(): List<Cookie> {
        val cookiesString = this[COOKIE_KEY] ?: return emptyList()
        return Json.decodeFromString(cookiesString)
    }

    private fun MutablePreferences.writeCookies(cookies: List<Cookie>): MutablePreferences {
        this[COOKIE_KEY] = Json.encodeToString(cookies)
        return this
    }

    //TODO: improve this
    // Função de correspondência de cookies com a URL
    private fun Cookie.matches(url: Url): Boolean {
            val hostMatches = domain?.let {
                url.host.endsWith(it, ignoreCase = true)
            } == true
            val pathMatches = path?.let {
                url.encodedPath.startsWith(it)
            } != false

            return hostMatches && pathMatches
        }
