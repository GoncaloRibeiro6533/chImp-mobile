package pt.isel.chimp

import android.app.Application
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.http.ChImpServiceHttp
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
}