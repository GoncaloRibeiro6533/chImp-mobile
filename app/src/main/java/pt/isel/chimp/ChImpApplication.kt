package pt.isel.chimp

import android.app.Application
import pt.isel.chimp.service.ChImpService

class ChImpApplication : Application(), DependenciesContainer {

    override val chImpService: ChImpService by lazy {
        ChImpService()
    }
}