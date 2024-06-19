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
data class NfsEntry(
    var index: Int? = null,
    val directory: String,
    val clients: Map<String, List<NfsOption>>
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
