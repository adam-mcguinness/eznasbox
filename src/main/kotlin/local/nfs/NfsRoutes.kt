package local.nfs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.nfsRoutes() {
    route("/nfs") {
        get {
            readNfsSharesFile(call)
        }
        post{
            addNewNfsShareEntry(call)
        }
    }
}