package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.utils.logger
import com.vsloong.apknurse.utils.runCMD
import localApkSignerJarPath
import localZipAlignPath
import java.io.File

/**
 * apk文件对齐，重签名用例
 */
class SignUseCase {


    /**
     * 对Apk文件对齐
     * @param srcApkPath 源apk文件的路径
     * @param outputApkPath 对齐后输出apk文件的路径
     * @return 对齐后的apk文件的路径
     */
    fun alignApk(
        srcApkPath: String,
        outputApkPath: String
    ): Boolean {

        var isAlignSuccess = false
        val result = runCMD(
            localZipAlignPath(),
            "-p",
            "-f",
            "-v",
            "4",
            srcApkPath,
            outputApkPath,
            onLine = {
                logger("Align APK : $it")
                if (it.contains("Verification succesful")) {
                    isAlignSuccess = true
                }
            }
        )
        return result == 0 && isAlignSuccess
    }

    /**
     * 对apk文件进行签名
     * @param alignedApkPath 对齐过的apk文件地址
     */
    fun signApk(
        alignedApkPath: String,
        outputApkPath: String,
        keyStorePath:String,
        keyStorePassword:String,
        keyAlias:String,
        keyPassword:String
    ): Boolean {


        var isSignSuccess = false

        if (!File(keyStorePath).exists()) {
            logger("未发现签名文件，请检测路径：$keyStorePath")
            return false
        }

        val result = runCMD(
            "java",
            "-jar",
            localApkSignerJarPath(),
            "sign",
            "--verbose",
            "--ks",
            keyStorePath,
            "--ks-pass",
            "pass:${keyStorePassword}",
            "--ks-key-alias",
            keyAlias,
            "--key-pass",
            "pass:${keyPassword}",
            "--out",
            outputApkPath,
            alignedApkPath,
            onLine = {
                logger("Sign APK : $it")
                if (it.contains("Signed")) {
                    isSignSuccess = true
                }
            }
        )
        return result == 0 && isSignSuccess
    }

    /**
     * 验证apk是否签名
     *
     * 注：当前只验证是否是V2签名
     */
    fun verifyApk(
        srcApkPath: String
    ): Boolean {
        var isSigned = false
        val result = runCMD(
            "java",
            "-jar",
            localApkSignerJarPath(),
            "verify",
            "-v",
            "--print-certs",
            srcApkPath,
            onLine = {
                logger("Verify APK = $it")
                if (it.contains("Verified using v2 scheme (APK Signature Scheme v2): true")) {
                    isSigned = true
                }
            }
        )
        return result == 0 && isSigned
    }

}