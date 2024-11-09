package pt.isel.chimp

import pt.isel.chimp.service.mock.ChImpServiceMock

class ChImpTestApplication {
    val chImpServiceMock: ChImpServiceMock by lazy {
        ChImpServiceMock()
    }
}