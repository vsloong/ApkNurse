package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.utils.logger
import com.vsloong.apknurse.utils.runCMD
import localProcyonJarPath
import java.io.File

/**
 *反编译Jar文件为Java文件
 */
class Jar2JavaUseCase {

    /**
     * 将输入jar文件或者包含jar的文件夹反编译为java源文件
     * @param jarPath jar文件或者包含jar文件的文件夹
     */
    fun jar2java(
        jarPath: String,
        outDirPath: String
    ) {
        val jarFile = File(jarPath)
        if (!jarFile.exists()) {
            throw Throwable("File not exits : $jarPath")
        }

        val outDir = File(outDirPath)
        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        if (jarFile.isFile) {
            if (!jarFile.name.endsWith(".jar")) {
                throw Throwable("Wrong file type, not a jar file.")
            }
            jar2java(jarFile, outDirPath)
        } else if (jarFile.isDirectory) {
            jarFile
                .listFiles { f ->
                    f.name.endsWith(".jar")
                }
                ?.forEach {
                    jar2java(it, outDirPath)
                }
        }
    }

    /**
     * 将jar文件反编译为java源文件
     */
    private fun jar2java(
        jarFile: File,
        outDirPath: String
    ) {
        runCMD(
            "java",
            "-jar",
            localProcyonJarPath(),
            "-o",
            outDirPath,
            jarFile.absolutePath,
            onLine = {
                logger("jar to java :$it")
            }
        )
    }
}