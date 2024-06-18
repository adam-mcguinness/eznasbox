package local

import io.ktor.server.application.*
import local.plugins.*
import io.github.smiley4.ktorswaggerui.SwaggerUI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureCors()
    install(SwaggerUI)
}
