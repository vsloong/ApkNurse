package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.utils.runCMD
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
     * @param dexPath
     *          如果是dex文件，直接执行转换任务
     *          如果是文件夹，则遍历该文件夹下的dex文件，顺序执行转换任务
     */
    fun dex2jar(
        dexPath: String,
        outDirPath: String,
    ) {
        val dexFile = File(dexPath)
        if (!dexFile.exists()) {
            throw Throwable("File not exits : $dexPath")
        }

        val outDir = File(outDirPath)
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        if (dexFile.isFile) {
            if (!dexFile.name.endsWith(".dex")) {
                throw Throwable("Wrong file type, not a dex file.")
            }
            dexFile2JarFile(dexFile, outDirPath)
        } else if (dexFile.isDirectory) {
            dexFile
                .listFiles { f ->
                    f.name.endsWith(".dex")
                }
                ?.forEach {
                    dexFile2JarFile(it, outDirPath)
                }
        }
    }

    /**
     * 将Dex文件转换为Jar文件
     */
    private fun dexFile2JarFile(
        dexFile: File,
        outJarDirPath: String,
    ) {

        val outJarName = "${dexFile.name}.jar"
        val outJarFile = File(outJarDirPath, outJarName)

        runCMD(
            winCMD = winPrefixCMD,
            unixCMD = unixPrefixCMD,
            cmdSuffix = arrayOf(
                dexFile.absolutePath,
                "-o",
                outJarFile.absolutePath
            ),
            directory = File(localDex2JarPath())
        )
    }
}