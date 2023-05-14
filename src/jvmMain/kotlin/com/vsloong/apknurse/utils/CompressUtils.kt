package com.vsloong.apknurse.utils

import java.io.File
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
        val zipInputStream = ZipInputStream(File(zipFilePath).inputStream())

        var zipEntry: ZipEntry? = zipInputStream.nextEntry
        while (zipEntry != null) {
            val newFile = File(outputDirPath, zipEntry.name)

            if (zipEntry.isDirectory) {
                newFile.mkdirs()
            } else {
                val parentDir = newFile.parentFile
                if (!parentDir.exists()) {
                    parentDir.mkdirs()
                }

                val outputStream = newFile.outputStream()
                var len: Int = zipInputStream.read(buffer)
                while (len > 0) {
                    outputStream.write(buffer, 0, len)
                    len = zipInputStream.read(buffer)
                }
                outputStream.close()
            }

            zipEntry = zipInputStream.nextEntry
        }

        zipInputStream.closeEntry()
        zipInputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}