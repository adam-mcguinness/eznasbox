package local.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import local.nfs.nfsRoutes
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*

fun Application.configureRouting() {
    routing {
        // Serve all static files from the /static directory
        staticResources("/", "static")
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml")
        swaggerUI(path = "docs", swaggerFile = "openapi/documentation.yaml")
        nfsRoutes()
    }
}
