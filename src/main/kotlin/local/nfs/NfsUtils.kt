package local.nfs

import java.io.File


fun File.parseNfsExports(): List<NfsEntry> {
    val entries = mutableListOf<NfsEntry>()
    val lines = this.readLines()

    for (line in lines) {
        if (line.isBlank()) continue

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
                    when (option.toUpperCase()) {
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
            builder.append(options.joinToString(",") { it.name.toLowerCase() })
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