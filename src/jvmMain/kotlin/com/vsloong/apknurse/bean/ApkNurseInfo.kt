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
     * 获得该工程的工程目录
     */
    fun getCurrentProjectDirPath(): String {
        return localProjectsDirPath() +
            File.separator + getCurrentProjectName()
    }

    /**
     * 获取APK文件经过解压后的工程目录地址
     */
    fun getDecompressDirPath(): String {
        return getCurrentProjectDirPath() +
            File.separator + "decompress"
    }

    /**
     * 获取APK文件经过ApkTool反编译后的工程目录地址
     */
    fun getDecodeDirPath(): String {
        return getCurrentProjectDirPath() +
            File.separator + "decode"
    }

    /**
     * 获取Dex文件反编译为jar文件后的文件夹
     */
    fun getDecompiledJarDirPath(): String {
        return getCurrentProjectDirPath() +
            File.separator + "decompiled_jar"
    }

    /**
     * 获取jar文件反编译为java文件后的文件夹
     */
    fun getDecompiledJavaDirPath(): String {
        return getCurrentProjectDirPath() +
            File.separator + "decompiled_java"
    }
}