package pt.isel.chimp.http.utils

import io.ktor.http.ContentDisposition.Companion.File
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.net.URL



fun fetchErrorFile(url: URI): String = url.toString().split("/").last().split("-").joinToString(" ")

/*
TODO
try {
     val connection = URL(url.toString()).openStream()
     BufferedReader(InputStreamReader(connection)).use { buffer ->
         buffer.readText()
     }
 } catch (e: Throwable) {
     throw ChImpException("Error fetching error file", e)
 }*/
