package pt.isel.chimp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.CookiesStorage
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.storage.ChImpClientDB

interface DependenciesContainer {
    val client : HttpClient
    val chImpService: ChImpService
    val userInfoRepository: UserInfoRepository
    val cookieRep: CookiesStorage
    val preferencesDataStore: DataStore<Preferences>
    val clientDB: ChImpClientDB
    val repo: ChImpRepo

}