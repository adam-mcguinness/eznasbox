package local.nfs

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File



suspend fun readNfsSharesFile(call: ApplicationCall) {
    val exportsFile = File("/home/adam/fileshare/etc/exports")
    if (exportsFile.exists()) {
        val nfsEntries = exportsFile.parseNfsExports()
        val jsonResponse = Json.encodeToString(nfsEntries)
        call.respondText(jsonResponse, ContentType.Application.Json)
    }
}

suspend fun updateNfsSharesFile(call: ApplicationCall) {
    val filePath = "/home/adam/fileshare/etc/exports"

    // Parse the received JSON body
    val nfsEntriesJson = call.receiveText()
    val nfsEntries: List<NfsEntry> = Json.decodeFromString(nfsEntriesJson)

    // Write to the file
    writeNfsEntriesToFile(nfsEntries, filePath)

    call.respondText("NFS entries written successfully", status = HttpStatusCode.OK)
}

