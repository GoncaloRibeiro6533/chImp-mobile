package pt.isel.chimp

import pt.isel.chimp.service.ChImpService

interface DependenciesContainer {
    val chImpService: ChImpService
}