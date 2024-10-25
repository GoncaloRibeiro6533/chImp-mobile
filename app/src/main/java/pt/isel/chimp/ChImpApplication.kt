package pt.isel.chimp

import android.app.Application
import pt.isel.chimp.service.ChImpServiceMock

class ChImpApplication : Application(), DependenciesContainer {

    override val chImpService: ChImpServiceMock by lazy {
        ChImpServiceMock()
    }
}