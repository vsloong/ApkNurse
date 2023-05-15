package com.vsloong.apknurse.utils

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * 解压zip文件
 */
fun decompressByZip(
    zipFilePath: String,
    outputDirPath: String
) {
    val buffer = ByteArray(1024)

    try {

        val outputDir = File(outputDirPath)
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val zipInputStream = ZipInputStream(File(zipFilePath).inputStream())
        var zipEntry: ZipEntry? = zipInputStream.nextEntry

        while (zipEntry != null) {

            // 有获取到结果为空字符串的情况
            if (zipEntry.name.isNullOrBlank()) {
                continue
            }

            val newFile = File(outputDirPath, zipEntry.name)

            if (zipEntry.isDirectory) {
                newFile.mkdirs()
            } else {
                val parentDir = newFile.parentFile
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs()
                }

                val bufferedOutputStream = BufferedOutputStream(FileOutputStream(newFile))
                var bytesRead: Int
                while (zipInputStream.read(buffer).also { bytesRead = it } != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead)
                }
                bufferedOutputStream.close()
            }

            zipEntry = zipInputStream.nextEntry
        }

        zipInputStream.closeEntry()
        zipInputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}