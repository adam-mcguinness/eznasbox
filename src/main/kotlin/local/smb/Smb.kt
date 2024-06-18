package local.smb

import kotlinx.serialization.Serializable

@Serializable
data class SmbConfig(
    var config: SmbGlobal,
    var shares: List<SmbEntry>
)

@Serializable
data class SmbEntry (
    var name: String = "name",
    var path: String = "/path",
    var browsable: Boolean = true,
    var writable: Boolean = true,
    var guestOk: Boolean? = true,
    var readOnly: Boolean? = false,
    var forceUser: String = "user",
    var validUsers: List<String>? = null,
    var invalidUsers: List<String>? = null,
    var validGroups: List<String>? = null,
    var invalidGroups: List<String>? = null,
    var createMask: String? = null,
    var directoryMask: String? = null,
    var forceGroup: String? = null,
    var forceCreateMode: String? = null,
    var forceDirectoryMode: String? = null,
    var forceSecurityMode: Boolean? = null
)

@Serializable
data class SmbGlobal (
    var workgroup: String = "WORKGROUP",
    var serverString: String = "Samba Server",
    var serverRole: String = "standalone server",
    var logFile: String = "/var/log/samba/log.%m",
    var maxLogSize: Int = 100,
    var security: String = "user",
    var mapToGuest: String = "Bad User",
    var minProtocol: String = "SMB2",
    var dnsProxy: Boolean? = null,
    var interfaces: List<String>? = null,
    var bindInterfacesOnly: Boolean? = null,
    var hostsAllow: List<String>? = null,
    var hostsDeny: List<String>? = null,
    var logLevel: Int? = null,
    var syslog: Int? = null,
    var maxConnections: Int? = null,
    var clientNTLMv2Auth: Boolean? = null
)
