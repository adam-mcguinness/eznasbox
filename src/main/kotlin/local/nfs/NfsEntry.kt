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
    var index: Int? = null,
    val directory: String,
    val client: String,
    val options: List<NfsOption> = listOf()
)
