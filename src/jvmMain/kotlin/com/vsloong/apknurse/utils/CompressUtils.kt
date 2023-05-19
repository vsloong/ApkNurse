package com.vsloong.apknurse.utils

import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.File
import java.io.FileOutputStream

/**
 * 解压zip文件
 */
fun decompressByZip(
    zipFilePath: String,
    outputDirPath: String
) {
    try {

        val outputDir = File(outputDirPath)
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        ZipFile(zipFilePath).use { zip ->
            zip.entries.asSequence().forEach { entry ->
                val entryFile = File(outputDir, entry.name)

                logger("decompress zip: ${entryFile.name}")

                // 确保父目录存在
                entryFile.parentFile?.mkdirs()

                if (entry.isDirectory) {
                    // 如果是目录，创建对应的目录
                    entryFile.mkdirs()
                } else {
                    // 如果是文件，将文件解压到目标目录
                    zip.getInputStream(entry).use { input ->
                        FileOutputStream(entryFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}