package pt.isel.chimp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.service.ChImpService

interface DependenciesContainer {
    val chImpService: ChImpService
    val userInfoRepository: UserInfoRepository
    val preferencesDataStore: DataStore<Preferences>

}