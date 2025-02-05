package pt.isel.chimp

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.sse.SSE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.infrastructure.CookiesRepo
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.repository.ChImpRepoImp
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.mock.ChImpServiceMock
import pt.isel.chimp.storage.ChImpClientDB
import kotlin.time.Duration.Companion.seconds

class ChImpTestApplication : Application(), DependenciesContainer {


    override val clientDB: ChImpClientDB by lazy {
        Room.inMemoryDatabaseBuilder(
            context = this,
            klass = ChImpClientDB::class.java
        ).build()
    }
    override val repo: ChImpRepo by lazy {
        ChImpRepoImp(
            clientDB,
            remote = chImpService
        )
    }
    override val client: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(HttpCookies){
                storage = cookieRep
            }
            install(SSE) {
                reconnectionTime = 5.seconds
                showCommentEvents()
                showRetryEvents()
            }


        }
    }
    override val chImpService: ChImpService by lazy {
        ChImpServiceMock(cookieRep as CookiesRepo)
    }
    override val cookieRep: CookiesStorage by lazy {
        CookiesRepo(preferencesDataStore)
    }
    override val preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "test_prefs")

    override val userInfoRepository: UserInfoRepository by lazy {
        UserInfoRepo(preferencesDataStore)
    }

}