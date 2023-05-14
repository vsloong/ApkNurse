package com.vsloong.apknurse.usecase

import com.vsloong.apknurse.utils.runCMD
import localProcyonJarPath

/**
 *反编译Jar文件为Java文件
 */
class ProcyonUseCase {

    fun decompile(
        jarFilePath: String,
        outDirPath: String
    ) {
        runCMD(
            "java",
            "-jar",
            localProcyonJarPath(),
            "-o",
            outDirPath,
            jarFilePath
        )
    }
}