package local.smb
import java.io.File

fun CreateSmbConfigFile (){
    val configFile = "./smb.conf"
    val defaultConfigValues = SmbGlobal.toString() + "\n" + SmbEntry.toString()
    File(configFile).writeText(defaultConfigValues)
}