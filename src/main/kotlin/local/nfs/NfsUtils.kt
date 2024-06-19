package local.nfs

import java.io.File


fun File.parseNfsExports(): List<NfsEntry> {
    val entries = mutableListOf<NfsEntry>()
    val lines = this.readLines()

    var index = 0  // Placeholder for the real index if it's specified elsewhere

    for (line in lines) {
        if (line.isBlank() || line.startsWith('#')) continue

        val parts = line.split("\\s+".toRegex())
        if (parts.size < 2) continue

        val directory = parts[0]
        val clients = mutableListOf<ClientOptions>()

        for (i in 1 until parts.size) {
            val clientAndOptions = parts[i].split("(", ")")
            if (clientAndOptions.size < 2) continue

            val client = clientAndOptions[0]
            val options = clientAndOptions[1]
                .split(",")
                .mapNotNull { option ->
                    when (option.uppercase()) {
                        "RW" -> NfsOption.RW
                        "RO" -> NfsOption.RO
                        "SYNC" -> NfsOption.SYNC
                        "ASYNC" -> NfsOption.ASYNC
                        "NO_ROOT_SQUASH" -> NfsOption.NO_ROOT_SQUASH
                        "ROOT_SQUASH" -> NfsOption.ROOT_SQUASH
                        "ALL_SQUASH" -> NfsOption.ALL_SQUASH
                        else -> null
                    }
                }
            clients.add(ClientOptions(client, options))
        }

        entries.add(NfsEntry(index = index, directory = directory, clients = clients))
        index++  // Increment index to simulate line numbers or identifiers if needed
    }

    return entries
}

fun List<NfsEntry>.toNfsExports(): String {
    val exportsMap = mutableMapOf<String, MutableMap<String, List<NfsOption>>>()

    // Group entries by directory since index is not needed for output
    for (entry in this) {
        val directory = entry.directory
        val clientMap = exportsMap.computeIfAbsent(directory) { mutableMapOf() }
        entry.clients.forEach { (client, options) ->
            clientMap.merge(client, options) { oldOptions, newOptions -> oldOptions + newOptions }
        }
    }

    // Build the export file content without index comments
    val builder = StringBuilder()
    for ((directory, clients) in exportsMap) {
        builder.append(directory)
        clients.forEach { (client, options) ->
            builder.append(" $client(")
            builder.append(options.distinct().joinToString(",") { it.name.lowercase() }) // Ensuring options are unique
            builder.append(")")
        }
        builder.appendLine()
    }

    return builder.toString().trim()
}



fun writeNfsEntriesToFile(nfsEntries: List<NfsEntry>, filePath: String) {
    val fileContent = nfsEntries.toNfsExports()
    File(filePath).writeText(fileContent)
}

fun updateNfsEntryInFile(nfsEntry: NfsEntry, filePath: String): Pair<NfsEntryUpdateStatus, NfsEntry> {
    val exportsFile = File(filePath)
    val entries: MutableList<NfsEntry>
    try {
        entries = exportsFile.parseNfsExports().toMutableList()
    } catch (e: Exception) {
        return NfsEntryUpdateStatus.ERROR to nfsEntry
    }

    val index = entries.indexOfFirst { it.index == nfsEntry.index }
    if (index != -1) {
        entries[index] = nfsEntry
        writeNfsEntriesToFile(entries, filePath)
        return NfsEntryUpdateStatus.UPDATED to nfsEntry
    } else {
        entries.add(nfsEntry)
        writeNfsEntriesToFile(entries, filePath)
        return NfsEntryUpdateStatus.ADDED to nfsEntry
    }
}

fun deleteNfsEntryInFile(index: Int, filePath: String): NfsEntryDeletionStatus {
    val exportsFile = File(filePath)
    val entries: MutableList<NfsEntry>
    try {
        entries = exportsFile.parseNfsExports().toMutableList()
    } catch (e: Exception) {
        // Handle possible I/O errors or parsing errors
        return NfsEntryDeletionStatus.ERROR
    }
    return if (index != -1) {
        entries.removeAt(index)
        writeNfsEntriesToFile(entries, filePath)
        NfsEntryDeletionStatus.DELETED
    } else {
        NfsEntryDeletionStatus.ERROR
    }
}