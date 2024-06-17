package local.nfs

import kotlinx.serialization.Serializable

@Serializable
enum class NfsOption {
    RW,
    RO,
    SYNC,
    ASYNC,
    NO_ROOT_SQUASH,
    ROOT_SQUASH,
    ALL_SQUASH
}


@Serializable
data class NfsEntry (
    val directory: String,
    val client: String,
    val options: List<NfsOption> = listOf()
)

enum class NfsEntryUpdateStatus {
    ADDED,
    UPDATED,
    ERROR // You might include this for handling any potential errors.
}

enum class NfsEntryDeletionStatus {
    DELETED,
    ERROR
}
