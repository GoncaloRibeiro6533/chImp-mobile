package pt.isel.chimp

import pt.isel.chimp.service.ChImpServiceMock

class ChImpTestApplication {
    val chImpServiceMock: ChImpServiceMock by lazy {
        ChImpServiceMock()
    }
}