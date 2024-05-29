package local.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import local.nfs.nfsRoutes

fun Application.configureRouting() {
    routing {
        // Serve all static files from the /static directory
        staticResources("/", "static")
        nfsRoutes()
    }
}
