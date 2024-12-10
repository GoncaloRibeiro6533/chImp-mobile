package pt.isel.chimp.service.mock

class CookieRepoMock {

    private val cookies = mutableMapOf<String, String>()

    fun setCookie(key: String, value: String) {
        cookies[key] = value
    }

    fun getCookie(key: String): String? {
        return cookies[key]
    }

    fun removeCookie(key: String) {
        cookies.remove(key)
    }

    fun clearCookies() {
        cookies.clear()
    }
}