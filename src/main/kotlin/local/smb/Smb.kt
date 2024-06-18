package local.smb

import kotlinx.serialization.Serializable

@Serializable
data class SmbConfig(
    val config: SmbGlobal,
    var shares: List<SmbEntry>
)

@Serializable
data class SmbEntry (
    var name: String,
    var directory: String?,
    var readOnly: Boolean?,
    var guestOk: Boolean?,
    var validUsers: List<String> = listOf(),
    var invalidUsers: List<String> = listOf(),
    var validGroups: List<String> = listOf(),
    var invalidGroups: List<String> = listOf(),
    var writable: Boolean = true,
    var browsable: Boolean = true,
    var createMask: String = "0755",
    var directoryMask: String = "0755",
    var forceUser: String = "",
    var forceGroup: String = "",
    var forceCreateMode: String = "0755",
    var forceDirectoryMode: String = "0755",
    var forceSecurityMode: Boolean = false
)

@Serializable
data class SmbGlobal (
    var mapToGuest: String = "Bad User",
    var security: String = "user",
    var workgroup: String = "WORKGROUP",
    var serverString: String = "Samba Server",
    var interfaces: List<String> = listOf(),
    var bindInterfacesOnly: Boolean = true,
    var hostsAllow: List<String> = listOf(),
    var hostsDeny: List<String> = listOf(),
    var logLevel: Int = 2,
    var maxLogSize: Int = 1000,
    var syslog: Int = 0,
    var logFile: String = "/var/log/samba/log.%m",
    var maxConnections: Int = 0,
    var minProtocol: String = "SMB2",
    var clientNTLMv2Auth: Boolean = true
)
