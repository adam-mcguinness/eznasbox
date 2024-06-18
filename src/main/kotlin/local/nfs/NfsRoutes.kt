package local.nfs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.github.smiley4.ktorswaggerui.dsl.routing.*

fun Route.nfsRoutes() {
    route("/nfs") {
        get({
            summary = "Shows All NFS shares"
            description = "This will return all of the nfs shares."
            tags = listOf("nfs")
            response {
                HttpStatusCode.OK to {
                    body<List<NfsEntry>>()
                }
            }
        }){
            readNfsSharesFile(call)
        }
        put({
            summary = "Update or Add NFS share"
            description = "This adds a new NFS share or updates an existing share if it exists."
            tags= listOf("nfs")
            request {
                body<NfsEntry>()
            }
            response {
                HttpStatusCode.OK to {
                    body<String> {
                        description = "NFS entry updated"
                    }
                }
                HttpStatusCode.InternalServerError to {
                    body<String> {
                        description = "NFS Entry not Written"
                    }
                }
            }
        }){
            addNewNfsShareEntry(call)
        }
        delete("{directory}",{
            summary = "test delete"
            description = "This will return all of the nfs shares."
            tags = listOf("nfs")
            request {
                pathParameter<String>("directory"){
                    required = true
                    allowEmptyValue = false
                }
            }
        }){
            deleteNfsShareEntry(call)
        }
    }
}