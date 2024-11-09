package pt.isel.chimp.http.models

import kotlinx.serialization.Serializable
import java.net.URI

const val MEDIA_TYPE = "application/problem+json"

data class Problem(val uri: URI)
