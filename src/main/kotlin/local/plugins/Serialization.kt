package local.plugins

import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json{
            encodeDefaults = true
            ignoreUnknownKeys = true
        })
    }
}
