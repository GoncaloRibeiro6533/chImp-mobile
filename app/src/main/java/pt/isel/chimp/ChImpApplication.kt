package pt.isel.chimp

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.http.ChImpServiceHttp
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.service.mock.ChImpServiceMock

class ChImpApplication : Application(), DependenciesContainer {

    private val client by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    override val chImpService: ChImpService by lazy {
        ChImpServiceMock()
        //ChImpServiceHttp(client = client)
    }


    override val preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    override val userInfoRepository: UserInfoRepository by lazy {
        UserInfoRepo(preferencesDataStore)
    }
}