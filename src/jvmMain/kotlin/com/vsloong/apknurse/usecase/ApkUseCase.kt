package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.bean.ApkBasicBean
import com.vsloong.apknurse.utils.decompressByZip
import com.vsloong.apknurse.utils.logger
import com.vsloong.apknurse.utils.runCMD
import localAapt2Path

/**
 * 处理APK文件的用例
 */
class ApkUseCase {

    /**
     * 从apk获取信息
     */
    fun getApkInfo(apkPath: String): ApkBasicBean {
        val apkBasicBean = ApkBasicBean()
        runCMD(
            localAapt2Path(),
            "dump",
            "badging",
            apkPath,
            onLine = {
                getApkInfoFromLine(it, apkBasicBean)
            })
        return apkBasicBean
    }

    /**
     * 解压APK文件
     */
    fun decompressApk(
        apkFilePath: String,
        outputDirPath: String
    ) {
        decompressByZip(
            zipFilePath = apkFilePath,
            outputDirPath = outputDirPath
        )
    }


    /**
     * 从命令行输出的信息中获取apk信息
     */
    fun getApkInfoFromLine(
        string: String,
        apkBasicBean: ApkBasicBean
    ) {

        /**
         * 获取信息
         */
        fun subInfo(string: String): String {
            return string.substring(
                string.indexOfFirst { it == '\'' } + 1,
                string.length - 1
            )
        }

        //例如：package: name='com.cooloongwu.jumphacker' versionCode='1' versionName='1.0'
        if (string.startsWith("package:")) {
            val info = string.split(("\\s+").toRegex())

            if (info.size < 4) {
                throw Throwable("AAPT2输出的package信息有变动，请检查")
            }

            val packageName = subInfo(info[1])
            logger("当前的包名是：${packageName}")
            apkBasicBean.packageName = packageName

            val versionCode = subInfo(info[2])
            logger("当前的版本号是：${versionCode}")
            apkBasicBean.versionCode = versionCode

            val versionName = subInfo(info[3])
            logger("当前的版本名是：${versionName}")
            apkBasicBean.versionName = versionName

        }
        //例如：application: label='JumpHacker' icon='res/mipmap-anydpi-v26/ic_launcher.xml'

        else if (string.startsWith("application-label:")) {
            val appName = subInfo(string)
            logger("当前的应用名是：${appName}")
            apkBasicBean.appName = appName
        }
    }
}