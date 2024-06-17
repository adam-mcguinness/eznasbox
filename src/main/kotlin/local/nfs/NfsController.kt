package local.nfs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Reads NFS shares from a file and responds with the content in JSON format.
 */
suspend fun readNfsSharesFile(call: ApplicationCall) {
    val exportsFile = File("./exports")
    if (exportsFile.exists()) {
        // Assume parseNfsExports is a function that parses the file into a list of NfsEntry
        val nfsEntries = exportsFile.parseNfsExports()
        // Convert the list of NfsEntry objects to a JSON string
        val jsonResponse = Json.encodeToString(nfsEntries)
        // Respond with the JSON representation of NFS entries
        call.respondText(jsonResponse, ContentType.Application.Json)
    } else {
        // Respond with an error if the file does not exist
        call.respond(HttpStatusCode.NotFound, "Exports file not found.")
    }
}

/**
 * Handles the addition of a new NFS share entry.
 */


suspend fun addNewNfsShareEntry(call: ApplicationCall){
    val newEntry = call.receive<NfsEntry>()

    val status = updateNfsEntryInFile(newEntry, "./exports")

    when (status) {
        NfsEntryUpdateStatus.UPDATED -> call.respond(HttpStatusCode.OK, "NFS entry updated")
        NfsEntryUpdateStatus.ADDED -> call.respond(HttpStatusCode.OK, "NFS entry added")
        NfsEntryUpdateStatus.ERROR -> call.respond(HttpStatusCode.InternalServerError, "NFS Entry not Written")
    }

}