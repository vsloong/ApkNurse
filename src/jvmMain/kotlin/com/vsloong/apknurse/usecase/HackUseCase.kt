package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.utils.logger
import com.vsloong.apknurse.utils.runCMD
import localApkToolJarPath
import java.io.File


/**
 * 编解码APK的用例
 */
class HackUseCase {


    /**
     * 解码APK到文件夹
     */
    fun decodeApk(
        apkPath: String,
        decodeDirPath: String
    ) {
        // 删除之前存在的文件夹，业务逻辑变得复杂了，还有插入垃圾资源的方案会有影响，每次清除文件即可
        val decodeDir = File(decodeDirPath)
        if (decodeDir.exists() && decodeDir.listFiles()?.isNotEmpty() == true) {
            logger("已经解码过该文件了，清空之前的文件夹")
            decodeDir.deleteRecursively()
        }

        runCMD(
            "java",
            "-jar",
            localApkToolJarPath(),
            "decode",
            apkPath,
            "-o",
            decodeDirPath,
            "-f",
            onLine = {
                logger("apk tool decode : $it")
            }
        )
    }

    /**
     * 根据APK的解码文件夹重新构建APK
     */
    fun buildApk(
        decodeDirPath: String,
        targetApkPath: String
    ) {
        runCMD(
            "java",
            "-jar",
            localApkToolJarPath(),
            "build",
            decodeDirPath,
            "-o",
            targetApkPath,
            onLine = {
                logger("apk tool build : $it")
            }
        )
    }
}