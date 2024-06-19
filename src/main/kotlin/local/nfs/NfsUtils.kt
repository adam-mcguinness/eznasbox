package local.nfs

import java.io.File


fun parseNfsExportsFile(filePath: String): List<NfsEntry> {
    val entries = mutableListOf<NfsEntry>()
    val file = File(filePath)
    var index = 0

    file.forEachLine { line ->
        // Trim and check if line is empty to skip any empty lines
        val trimmedLine = line.trim()
        if (trimmedLine.isNotEmpty()) {
            val firstSpaceIndex = trimmedLine.indexOf(' ')
            if (firstSpaceIndex != -1) {
                val directory = trimmedLine.substring(0, firstSpaceIndex)
                val clientWithOptions = trimmedLine.substring(firstSpaceIndex).trim()
                val client = clientWithOptions.substringBefore('(').trim()
                val optionsStr = clientWithOptions.substringAfter('(', "").substringBefore(')').trim()
                val options = optionsStr.split(',').mapNotNull { option ->
                    try {
                        NfsOption.valueOf(option.uppercase())
                    } catch (e: IllegalArgumentException) {
                        null // Skip unknown options
                    }
                }
                entries.add(NfsEntry(index, directory, client, options))
                index++ // Increment index for each line processed
            }
        }
    }
    return entries
}

fun writeNfsExportsFile(entries: List<NfsEntry>, filePath: String) {
    val file = File(filePath)

    file.bufferedWriter().use { out ->
        entries.forEach { entry ->
            val optionsStr = entry.options.joinToString(",") { it.name.lowercase() }
            out.write("${entry.directory} ${entry.client}($optionsStr)\n")
        }
    }
}