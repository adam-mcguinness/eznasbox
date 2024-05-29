package local.smb

import kotlinx.serialization.Serializable


@Serializable
data class SmbEntry (
    val name: String,
    val directory: String,
    val readOnly: Boolean,
    val guestOk: Boolean,
    val validUsers: List<String> = listOf(),
    val invalidUsers: List<String> = listOf(),
    val validGroups: List<String> = listOf(),
    val invalidGroups: List<String> = listOf(),
    val writable: Boolean = true,
    val browsable: Boolean = true,
    val createMask: String = "0755",
    val directoryMask: String = "0755",
    val forceUser: String = "",
    val forceGroup: String = "",
    val forceCreateMode: String = "0755",
    val forceDirectoryMode: String = "0755",
    val forceSecurityMode: Boolean = false
)

@Serializable
data class SmbGlobal (
    val mapToGuest: String = "Bad User",
    val security: String = "user",
    val workgroup: String = "WORKGROUP",
    val serverString: String = "Samba Server",
    val interfaces: List<String> = listOf(),
    val bindInterfacesOnly: Boolean = true,
    val hostsAllow: List<String> = listOf(),
    val hostsDeny: List<String> = listOf(),
    val logLevel: Int = 2,
    val maxLogSize: Int = 1000,
    val syslog: Int = 0,
    val logFile: String = "/var/log/samba/log.%m",
    val maxConnections: Int = 0,
    val minProtocol: String = "SMB2",
    val clientNTLMv2Auth: Boolean = true
)
