import com.vsloong.apknurse.usecase.HackUseCase
import com.vsloong.apknurse.usecase.SignUseCase
import com.vsloong.apknurse.usecase.XmlUseCase

fun main() {
    testXml()
    testBuild()
    testSign()
}


val currentProjectName = "com.woome.blissu_10_2023_06_20_16_42_32_574"

private fun testXml() {
    val xmlUseCase = XmlUseCase()


    // 生成安全配置文件
    val outputFilePath = "/Users/dragon/ApkNurse/projects/" +
        "${currentProjectName}/decode/res/xml/network_security_config.xml"
    xmlUseCase.createNetWorkSecurityConfigXml(
        outputFilePath = outputFilePath
    )

    // 配置安全配置文件
    xmlUseCase.addNetWorkSecurityConfigToManifest(
        xmlFilePath = "/Users/dragon/ApkNurse/projects/" +
            "${currentProjectName}/decode/AndroidManifest.xml",
        outputFilePath = "/Users/dragon/ApkNurse/projects/" +
            "${currentProjectName}/decode/AndroidManifest.xml",
    )
}

private fun testBuild() {
    val hackUseCase = HackUseCase()

    val decodeDirPath = "/Users/dragon/ApkNurse/projects/" +
        "${currentProjectName}/decode"
    hackUseCase.buildApk(
        decodeDirPath = decodeDirPath,
        targetApkPath = "/Users/dragon/ApkNurse/projects/" +
            "${currentProjectName}/build/new.apk"
    )
}

private fun testSign() {

    val newApkPath = "/Users/dragon/ApkNurse/projects/" +
        "${currentProjectName}/build/new.apk"

    val alignedApkPath = newApkPath.replace(".apk", "_align.apk")
    val signedApkPath = alignedApkPath.replace(".apk", "_sign.apk")

    val signUseCase = SignUseCase()

    signUseCase.alignApk(
        srcApkPath = newApkPath,
        outputApkPath = alignedApkPath
    )

    signUseCase.signApk(
        alignedApkPath = alignedApkPath,
        outputApkPath = signedApkPath,
        keyStorePath = localDebugKeyStorePath(),
        keyStorePassword = "vsloong",
        keyAlias = "vsloong",
        keyPassword = "vsloong"
    )
}