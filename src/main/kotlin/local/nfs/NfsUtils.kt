package local.nfs

import java.io.File


fun File.parseNfsExports(): List<NfsEntry> {
    val entries = mutableListOf<NfsEntry>()
    val lines = this.readLines()

    for (line in lines) {
        if (line.isBlank()) continue
        if (line.startsWith('#')) continue

        val parts = line.split("\\s+".toRegex())
        if (parts.size < 2) continue

        val directory = parts[0]

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
            entries.add(NfsEntry(directory, client, options))
        }
    }

    return entries
}

fun List<NfsEntry>.toNfsExports(): String {
    val exportsMap = mutableMapOf<String, MutableList<Pair<String, List<NfsOption>>>>()

    // Group entries by directory
    for (entry in this) {
        exportsMap.computeIfAbsent(entry.directory) { mutableListOf() }
            .add(entry.client to entry.options)
    }

    // Build the export file content
    val builder = StringBuilder()
    for ((directory, clients) in exportsMap) {
        builder.append(directory)
        for ((client, options) in clients) {
            builder.append(" $client(")
            builder.append(options.joinToString(",") { it.name.lowercase() })
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

fun updateNfsEntryInFile(nfsEntry: NfsEntry, filePath: String): NfsEntryUpdateStatus {
    val exportsFile = File(filePath)
    val entries: MutableList<NfsEntry>
    try {
        entries = exportsFile.parseNfsExports().toMutableList()
    } catch (e: Exception) {
        // Handle possible I/O errors or parsing errors
        return NfsEntryUpdateStatus.ERROR
    }

    val index = entries.indexOfFirst { it.directory == nfsEntry.directory && it.client == nfsEntry.client }
    return if (index != -1) {
        entries[index] = nfsEntry
        writeNfsEntriesToFile(entries, filePath)
        NfsEntryUpdateStatus.UPDATED
    } else {
        entries.add(nfsEntry)
        writeNfsEntriesToFile(entries, filePath)
        NfsEntryUpdateStatus.ADDED
    }
}