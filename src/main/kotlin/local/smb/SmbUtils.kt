package local.smb
import java.io.File

fun readSmbConfigFromFile(): SmbConfig {
    val file = File("./smb.conf")
    val lines = file.readLines()
    val shares = mutableListOf<SmbEntry>()
    var global = SmbGlobal()
    var currentShare: SmbEntry? = null

    lines.forEach { line ->
        if (line.trim().startsWith("[") && line.trim().endsWith("]")) {
            val section = line.trim().substring(1, line.trim().length - 1)
            if (section.equals("global", ignoreCase = true)) {
                currentShare = null  // Ensure no current share is set when in global section
            } else {
                // Finish the previous share
                currentShare?.let { shares.add(it) }
                // Start a new share
                currentShare = SmbEntry(name = section,"",true, true)
            }
        } else if (!line.trim().startsWith(";") && line.contains("=")) {
            val parts = line.trim().split("=", limit = 2)
            if (parts.size == 2) {
                val key = parts[0].trim()
                val value = parts[1].trim()
                if (currentShare == null) {
                    // Global configuration settings
                    when (key.lowercase()) {
                        "workgroup" -> global.workgroup = value
                        "server string" -> global.serverString = value
                        "security" -> global.security = value
                        "map to guest" -> global.mapToGuest = value
                        "log file" -> global.logFile = value
                        "max log size" -> global.maxLogSize = value.toIntOrNull() ?: global.maxLogSize
                    }
                } else {
                    // Share specific settings
                    when (key.lowercase()) {
                        "path" -> currentShare!!.directory = value
                        "writable" -> currentShare!!.writable = (value.lowercase() == "yes")
                        "guest ok" -> currentShare!!.guestOk = (value.lowercase() == "yes")
                        "read only" -> currentShare!!.readOnly = (value.lowercase() == "no")
                        "create mask" -> currentShare!!.createMask = value
                        "directory mask" -> currentShare!!.directoryMask = value
                        "force user" -> currentShare!!.forceUser = value
                        "force group" -> currentShare!!.forceGroup = value
                    }
                }
            }
        }
    }
    // Add the last share if exists
    currentShare?.let { shares.add(it) }

    return SmbConfig(global, shares)
}

fun writeSmbConfigToFile(config: SmbConfig) {
    val builder = StringBuilder()

    // Write global settings
    builder.append("[global]\n")
    with(config.config) {
        builder.append("   workgroup = $workgroup\n")
        builder.append("   server string = $serverString\n")
        builder.append("   security = $security\n")
        builder.append("   map to guest = $mapToGuest\n")
        builder.append("   log file = $logFile\n")
        builder.append("   max log size = $maxLogSize\n")
    }

    // Write shares
    config.shares.forEach { share ->
        builder.append("\n[${share.name}]\n")
        builder.append("   path = ${share.directory}\n")
        builder.append("   writable = ${if (share.writable) "yes" else "no"}\n")
        builder.append("   guest ok = ${if (share.guestOk == true) "yes" else "no"}\n")
        builder.append("   read only = ${if (share.readOnly == true) "no" else "yes"}\n")
        builder.append("   create mask = ${share.createMask}\n")
        builder.append("   directory mask = ${share.directoryMask}\n")
        builder.append("   force user = ${share.forceUser}\n")
        builder.append("   force group = ${share.forceGroup}\n")
    }

    File("./smb.conf").writeText(builder.toString())
}