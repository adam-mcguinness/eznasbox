package local.smb

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.github.smiley4.ktorswaggerui.dsl.routing.*
import io.ktor.http.*

fun Route.smbRoutes() {
    route("/smb-config") {
        get({
            summary = "Smb Config"
            description = "Smb configuration"
            tags = listOf("smb config")
            response {
                HttpStatusCode.OK to {
                    body<SmbGlobal>()
                }
            }
        }) {
            readSmbGlobalConfigSettingsFromFile(call)
        }
        post({
            summary = "Smb Config"
            description = "Smb configuration"
            tags = listOf("smb config")
            request {
                body<SmbGlobal>()
            }
        }) {
            updateSmbGlobalConfigSettings(call)
        }
    }
    route("/smb/shares") {
        get({
            summary = "Smb Shares"
            description = "Smb shares"
            tags = listOf("smb shares")
            response {
                HttpStatusCode.OK to {
                    body<List<SmbEntry>>()
                }
            }
        }) {
            readSmbSharesFromFile(call)
        }
        put({
            summary = "Smb Shares"
            description = "Smb shares"
            tags = listOf("smb shares")
            request {
                body<SmbEntry>()
            }
            response {
                HttpStatusCode.OK to {
                    description = "Item successfully Updated"
                }
                HttpStatusCode.InternalServerError to {
                    description = "An Error occurred"
                }
            }
        }) {
            updateSmbShares(call)
        }
        delete("{name}",{
            summary = "Smb Shares"
            description = "Smb shares"
            tags = listOf("smb shares")
            request {
                pathParameter<String>("name"){
                    required = true
                    allowEmptyValue = false
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Item deleted successfully"
                }
                HttpStatusCode.InternalServerError to {
                    description = "An Error occurred"
                }
            }
        }) {
            removeSmbShare(call)
        }
    }
}