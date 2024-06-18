package local.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import local.nfs.nfsRoutes
import local.smb.smbRoutes


fun Application.configureRouting() {
    routing {
        // Serve all static files from the /static directory
        route("/",{
            hidden = true
        }){
            staticResources("/","static")
        }

        route("docs") {
            swaggerUI("/api.json")
        }

        route("api.json") {
            openApiSpec()
        }

        nfsRoutes()
        smbRoutes()
    }
}
