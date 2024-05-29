package local.smb

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.smbRoutes() {
    route("/smb-config") {
        get {
            readSmbGlobalConfigSettingsFromFile(call)
        }
        post {
            updateSmbGlobalConfigSettings(call)
        }
    }
    route("/smb/shares") {
        get {
            readSmbSharesFromFile(call)
        }
        put {
            updateSmbShares(call)
        }
        delete {
            removeSmbShare(call)
        }
    }
}