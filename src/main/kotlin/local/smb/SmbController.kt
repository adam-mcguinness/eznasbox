package local.smb

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun readSmbGlobalConfigSettingsFromFile(call: ApplicationCall) {
    val parsedFile = readSmbConfigFromFile()
    call.respond(parsedFile.config)
}

suspend fun updateSmbGlobalConfigSettings(call: ApplicationCall) {
    val config = call.receive<SmbGlobal>()
    val parsedFile = readSmbConfigFromFile()
    parsedFile.config = config
    writeSmbConfigToFile(parsedFile)
    call.respond(HttpStatusCode.OK, "successfully updated smb global config")
}

suspend fun readSmbSharesFromFile(call: ApplicationCall) {
    val parsedFile = readSmbConfigFromFile()
    call.respond(parsedFile.shares)
}
suspend fun updateSmbShares(call: ApplicationCall) {
    val share = call.receive<SmbEntry>()
    val configFile = readSmbConfigFromFile()
    val shares = configFile.shares.toMutableList()

    val index = shares.indexOfFirst { it.name == share.name }
    if (index != -1) {
        shares[index] = share
        configFile.shares = shares
        writeSmbConfigToFile(configFile)
        call.respond(HttpStatusCode.OK, "successfully updated smb share ${share.name}")
    } else{
        shares.add(share)
        configFile.shares = shares
        writeSmbConfigToFile(configFile)
        call.respond(HttpStatusCode.OK, "successfully added smb share ${share.name}")
    }
}

suspend fun removeSmbShare(call: ApplicationCall) {
    val share = call.parameters["name"]
    val configFile = readSmbConfigFromFile()
    val shares = configFile.shares.toMutableList()
    val index = shares.indexOfFirst { it.name == share }
    if (index != -1) {
        shares.removeAt(index)
        configFile.shares = shares
        writeSmbConfigToFile(configFile)
        call.respond(HttpStatusCode.OK, "$share has been successfully removed")
    } else {
        call.respond(HttpStatusCode.NotFound, "could not find share $share")
    }
}