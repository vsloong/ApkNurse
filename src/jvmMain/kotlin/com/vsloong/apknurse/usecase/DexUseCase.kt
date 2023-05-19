package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.utils.logger
import com.vsloong.apknurse.utils.runCMD
import jadx.api.JadxArgs
import jadx.api.JadxDecompiler
import java.io.File


/**
 * 处理Dex文件的用例
 */
class DexUseCase {


    /**
     * 将Dex文件转换为Java源文件
     * @param dexPath
     *          如果是dex文件，直接执行转换任务
     *          如果是文件夹，则遍历该文件夹下的dex文件，顺序执行转换任务
     */
    fun dex2java(
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
            dexFile2java(dexFile, outDirPath)
        } else if (dexFile.isDirectory) {
            dexFile
                .listFiles { f ->
                    f.name.endsWith(".dex")
                }
                ?.forEach {
                    dexFile2java(it, outDirPath)
                }
        }
    }

    /**
     * 使用Jadx将dex文件反编译为java源文件
     */
    private fun dexFile2java(
        dexFile: File,
        outDirPath: String
    ) {
        val jadxArgs = JadxArgs()
        jadxArgs.setInputFile(dexFile)
        jadxArgs.outDir = File(outDirPath)
        try {
            JadxDecompiler(jadxArgs).use { jadx ->
                jadx.load()
                jadx.save()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}