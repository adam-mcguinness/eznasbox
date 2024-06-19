package local.nfs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun readNfsSharesFile(call: ApplicationCall) {
    val exportsFile = "./exports"
    val shares = parseNfsExportsFile(exportsFile)
    call.respond(shares)
}

suspend fun addNewNfsShareEntry(call: ApplicationCall){
    val exportsFile = "./exports"
    val shares = parseNfsExportsFile(exportsFile).toMutableList()
    val newShare = call.receive<NfsEntry>()
    val index = newShare.index
    if (index == null) {
        newShare.index = shares.size
        shares.add(newShare)
        writeNfsExportsFile(shares, exportsFile)
        call.respond(HttpStatusCode.OK,newShare)
    } else {
        shares[index] = newShare
        writeNfsExportsFile(shares, exportsFile)
        call.respond(HttpStatusCode.OK,newShare)
    }
}

suspend fun deleteNfsShareEntry(call: ApplicationCall){
    val exportsFile = "./exports"
    val shares = parseNfsExportsFile(exportsFile).toMutableList()
    val index = call.parameters["index"]?.toInt()
    if (index != null) {
        if (index >= 0 && index < shares.size){
            shares.removeAt(index)
            writeNfsExportsFile(shares, exportsFile)
            call.respond(HttpStatusCode.OK, "item at index $index deleted")
        }else{
            call.respond(HttpStatusCode.NotFound)
        }
    }
}