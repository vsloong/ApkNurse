package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.utils.runCMD
import localApkToolJarPath
import localDex2JarPath
import java.io.File


/**
 * 处理Dex文件的用例
 */
class DexUseCase {

    private val winPrefixCMD = arrayOf("cmd", "/c", "d2j-dex2jar.bat")
    private val unixPrefixCMD = arrayOf("./d2j-dex2jar.sh")

    /**
     * 将Dex文件转换为Jar文件
     */
    fun dex2jar(
        dexFilePath: String,
        outJarFilePath: String,
    ) {
        runCMD(
            winCMD = winPrefixCMD,
            unixCMD = unixPrefixCMD,
            cmdSuffix = arrayOf(
                dexFilePath,
                "-o",
                outJarFilePath
            ),
            directory = File(localDex2JarPath())
        )
    }
}