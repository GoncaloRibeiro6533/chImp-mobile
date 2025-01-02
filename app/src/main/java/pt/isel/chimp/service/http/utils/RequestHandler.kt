package pt.isel.chimp.service.http.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import pt.isel.chimp.ChImpApplication
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.service.http.models.Problem
import pt.isel.chimp.service.http.models.ProblemDTO
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success


const val MEDIA_TYPE = "application/json"
val BASE_URL = "${ChImpApplication.Companion.NGROK}/api"
const val TOKEN_TYPE = "Bearer"
const val ERROR_MEDIA_TYPE = "application/problem+json"
const val SCHEME = "bearer"
const val NAME_WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"

class ChImpException(message: String?, cause: Throwable?) : Exception(message, cause)

// Main GET request function
suspend inline fun <reified T : Any> HttpClient.get(
    url: String,
    token: String = ""
): Either<ApiError, T> {
    return try {
        get(BASE_URL + url) {
            if (token.isNotEmpty()) header("Authorization", "$TOKEN_TYPE $token")
            header("Content-Type", MEDIA_TYPE)
            header("Accept", "$MEDIA_TYPE, $ERROR_MEDIA_TYPE")
        }.processResponse()
    } catch (e: Exception) {
        failure(ApiError("Unexpected error: ${e.message ?: e.cause?.message}"))
    }
}

suspend inline fun <reified T : Any> HttpClient.post(
    url: String,
    token: String = "",
    body: Any? = null
): Either<ApiError, T> {
    return try {
        post(BASE_URL + url) {
            if (token.isNotEmpty()) header("Authorization", "$TOKEN_TYPE $token")
            header("Content-Type", MEDIA_TYPE)
            header("Accept", "$MEDIA_TYPE, $ERROR_MEDIA_TYPE")

            if (body != null) setBody(body)
        }.processResponse()
    } catch (e: Exception) {
        failure(ApiError("Unexpected error: ${e.message ?: e.cause?.message}"))
    }
}

suspend inline fun <reified T : Any> HttpClient.put(
    url: String,
    token: String = "",
    body: Any? = null
): Either<ApiError, T> {
    return try {
        put(BASE_URL + url) {
            if (token.isNotEmpty()) header("Authorization", "$TOKEN_TYPE $token")
            header("Content-Type", MEDIA_TYPE)
            header("Accept", "$MEDIA_TYPE, $ERROR_MEDIA_TYPE")

            if (body != null) setBody(body)
        }.processResponse()
    } catch (e: Exception) {
        failure(ApiError("Unexpected error: ${e.message ?: e.cause?.message}"))
    }
}

// Function to process the HTTP response
suspend inline fun <reified T : Any> HttpResponse.processResponse(): Either<ApiError, T> {
    try {
        if (this.status.value == 200 && this.headers[HttpHeaders.ContentLength]?.toInt() == 0) return success(Unit as T)
        if (this.headers[NAME_WWW_AUTHENTICATE_HEADER] != null) throw ChImpException("Failed to authenticate", null)
        return when (this.headers[HttpHeaders.ContentType]) {
            ERROR_MEDIA_TYPE -> {
                val problem: Problem = this.body<ProblemDTO>().toProblem()
                failure(ApiError(fetchErrorFile(problem.uri)))
            }
            MEDIA_TYPE -> {
                val body: T = this.body<T>()
                success(body)
            }
            else -> {
               if (this.status.value == 400 || this.status.value == 404 || this.status.value == 502)
                   failure(ApiError("Failed to process request")) else
                     failure(ApiError("Unexpected error"))
            }
        }
    } catch (e: Exception) {
        throw ChImpException(null, e)
    }
}
