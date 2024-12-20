package pt.isel.chimp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import io.ktor.client.plugins.cookies.CookiesStorage
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.infrastructure.CookiesRepo
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.service.mock.ChImpServiceMock
import pt.isel.chimp.service.mock.CookieRepoMock
/*
class ChImpTestApplication {
    val chImpServiceMock: ChImpServiceMock by lazy {
        ChImpServiceMock(cookieStorage)
    }
    val preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    val userInfoRepository: UserInfoRepository by lazy {
        UserInfoRepo(preferencesDataStore)
    }
    val cookieStorage: CookiesStorage by lazy {  CookiesRepo() }
}*/