package local.smb
import java.io.File

fun readSmbConfigFromFile(): SmbConfig {
    val file = File("./smb.conf")
    if (!file.exists()) {
        return createAndWriteDefaultConfig()
    }

    val lines = file.readLines()
    val shares = mutableListOf<SmbEntry>()
    val global = SmbGlobal()
    var currentShare: SmbEntry? = null

    lines.forEach { line ->
        if (line.trim().startsWith("[") && line.trim().endsWith("]")) {
            val section = line.trim().substring(1, line.trim().length - 1)
            if (section.equals("global", ignoreCase = true)) {
                currentShare = null  // Ensure no current share is set when in global section
            } else {
                // Finish the previous share
                currentShare?.let { shares.add(it) }
                // Start a new share with the correct name
                currentShare = SmbEntry(name = section) // Ensure the share name is set correctly here
            }
        } else if (!line.trim().startsWith(";") && line.contains("=")) {
            val parts = line.trim().split("=", limit = 2)
            if (parts.size == 2) {
                val key = parts[0].trim()
                val value = parts[1].trim()
                if (currentShare == null) {
                    // Global configuration settings
                    setGlobalConfig(global, key, value)
                } else {
                    // Share specific settings
                    setShareConfig(currentShare!!, key, value)
                }
            }
        }
    }
    // Add the last share if exists
    currentShare?.let { shares.add(it) }

    return SmbConfig(global, shares)
}

fun setGlobalConfig(global: SmbGlobal, key: String, value: String) {
    when (key.lowercase()) {
        "workgroup" -> global.workgroup = value
        "server string" -> global.serverString = value
        "security" -> global.security = value
        "map to guest" -> global.mapToGuest = value
        "log file" -> global.logFile = value
        "max log size" -> global.maxLogSize = value.toIntOrNull() ?: global.maxLogSize
    }
}

fun setShareConfig(share: SmbEntry, key: String, value: String) {
    when (key.lowercase()) {
        "path" -> share.path = value
        "writable" -> share.writable = (value.lowercase() == "yes")
        "guest ok" -> share.guestOk = (value.lowercase() == "yes")
        "read only" -> share.readOnly = (value.lowercase() == "no")
        "create mask" -> share.createMask = value
        "directory mask" -> share.directoryMask = value
        "force user" -> share.forceUser = value
        "force group" -> share.forceGroup = value
    }
}

fun writeSmbConfigToFile(config: SmbConfig) {
    val builder = StringBuilder()
    val uniqueShares = config.shares.distinctBy { it.name }

    fun writeLine(prefix: String, value: Any?) {
        when (value) {
            is Boolean -> builder.append("   $prefix = ${if (value) "yes" else "no"}\n")
            null -> {} // Do nothing if value is null
            else -> builder.append("   $prefix = $value\n")
        }
    }

    // Write global settings
    builder.append("[global]\n")
    with(config.config) {
        writeLine("workgroup", workgroup)
        writeLine("server string", serverString)
        writeLine("server role",serverRole)
        writeLine("log file",logFile)
        writeLine("max log size",maxLogSize)
        writeLine("security",security)
        writeLine("map to guest",mapToGuest)
    }

    // Write shares
    uniqueShares.forEach { share ->
        builder.append("\n[${share.name}]\n")
        writeLine("path", share.path)
        writeLine("browsable",share.browsable)
        writeLine("writable", share.writable)
        writeLine("guest ok",share.guestOk)
        writeLine("read only",share.readOnly)
        writeLine("force user",share.forceUser)
        writeLine("create mask",share.createMask)
        writeLine("directory mask",share.directoryMask)
        writeLine("force group",share.forceGroup)
    }

    File("./smb.conf").writeText(builder.toString())
}

fun createAndWriteDefaultConfig(): SmbConfig {
    val defaultGlobal = SmbGlobal(
        workgroup = "WORKGROUP",
        serverString = "Samba Server",
        serverRole = "standalone server",
        logFile = "/var/log/samba/log.%m",
        maxLogSize = 100,
        security = "user",
        mapToGuest = "Bad User"
    )
    val defaultShare = SmbEntry(
        name = "Default Share",
        path = "/default/path",
        browsable = true,
        writable = true,
        guestOk = true
    )
    val config = SmbConfig(defaultGlobal, listOf(defaultShare))
    writeSmbConfigToFile(config) // Write the default config to file
    return config
}