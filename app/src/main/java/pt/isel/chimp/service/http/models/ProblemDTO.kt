package pt.isel.chimp.service.http.models

import kotlinx.serialization.Serializable
import java.net.URI

@Serializable
data class ProblemDTO(
    val type: String,
) {
    fun toProblem() = Problem(URI(type))
}