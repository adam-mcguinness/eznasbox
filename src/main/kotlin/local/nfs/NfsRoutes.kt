package local.nfs

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import local.nfs.readNfsSharesFile
import local.nfs.updateNfsSharesFile

fun Route.nfsRoutes() {
    route("/nfs") {
        get {
            readNfsSharesFile(call)
        }
        post {
            updateNfsSharesFile(call)
        }
    }
}