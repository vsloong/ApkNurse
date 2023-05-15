package com.vsloong.apknurse.bean

import com.vsloong.apknurse.utils.fileTimeMillis
import localProjectsDirPath
import java.io.File

/**
 * ApkNurse对应的实体类
 */
data class ApkNurseInfo(
    val apkBasicInfo: ApkBasicInfo? = null,
    val timestamp: Long = System.currentTimeMillis(),
) {

    /**
     * 获取当前工程的名称
     */
    private fun getCurrentProjectName(): String {
        if (apkBasicInfo != null) {
            return "${apkBasicInfo.packageName}_${apkBasicInfo.versionCode}_${fileTimeMillis(timestamp)}"
        }
        return "noApkInfo"
    }

    /**
     * 获取APK文件经过解压后的工程目录地址
     */
    fun getDecompressDirPath(): String {
        return localProjectsDirPath() +
            File.separator + getCurrentProjectName() +
            File.separator + "decompress"
    }

    /**
     * 获取APK文件经过ApkTool反编译后的工程目录地址
     */
    fun getDecodeDirPath(): String {
        return localProjectsDirPath() +
            File.separator + getCurrentProjectName() +
            File.separator + "decode"
    }

    /**
     * 获取Dex文件反编译为jar文件，再反编译为java文件的文件夹
     */
    fun getDecompileDirPath(): String {
        return localProjectsDirPath() +
            File.separator + getCurrentProjectName() +
            File.separator + "decompile"
    }
}