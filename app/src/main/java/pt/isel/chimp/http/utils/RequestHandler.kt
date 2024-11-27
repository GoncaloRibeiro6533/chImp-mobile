package pt.isel.chimp.http.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import pt.isel.chimp.http.models.Problem
import pt.isel.chimp.http.models.ProblemDTO
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.failure
import pt.isel.chimp.utils.success

data class ApiError(val message: String)

const val NGROK = "https://1356-2001-8a0-7efc-e400-24dd-a0a2-4738-2e47.ngrok-free.app"


const val MEDIA_TYPE = "application/json"
val BASE_URL = //"http://localhost:8080/api"
    "$NGROK/api"
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
        }.processResponse()
    } catch (e: Exception) {
        failure(ApiError("Unexpected error: ${e.message ?: e.cause?.message }"))
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
            if (body != null) setBody(body)
        }.processResponse()
    } catch (e: Exception) {
        failure(ApiError("Unexpected error: ${e.message ?: e.cause?.message }"))
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
            if (body != null) setBody(body)
        }.processResponse()
    } catch (e: Exception) {
        failure(ApiError("Unexpected error: ${e.message ?: e.cause?.message }"))
    }
}

// Function to process the HTTP response
suspend inline fun <reified T : Any> HttpResponse.processResponse(): Either<ApiError, T> {
    try {
        if (this.status.value == 200 && this.headers[HttpHeaders.ContentLength]?.toInt() == 0) return success(Unit as T) //TODO improve
        if (this.headers[NAME_WWW_AUTHENTICATE_HEADER] != null) throw ChImpException("Failed to authenticate", null)
        return when (this.headers[HttpHeaders.ContentType]) { //TODO check status also, and can validate only by content type?
            ERROR_MEDIA_TYPE -> {
                val problem: Problem = this.body<ProblemDTO>().toProblem()
                failure(ApiError(fetchErrorFile(problem.uri)))
            }
            MEDIA_TYPE -> {
                val body: T = this.body<T>()
                success(body)
            }
            else -> {
                throw ChImpException("Unexpected content type", null)
            }
        }
    } catch (e: Exception) {
        throw ChImpException(null, e)
    }
}
