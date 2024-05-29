package local.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCors() {
    install(CORS) {
        anyHost()
    }
}