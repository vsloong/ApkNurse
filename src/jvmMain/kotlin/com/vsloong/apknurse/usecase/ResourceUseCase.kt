package com.vsloong.apknurse.usecase

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.ResourceLoader
import com.vsloong.apknurse.utils.*
import localLibsDirPath
import localWorkspaceDirPath
import java.io.File
import java.io.FileOutputStream

/**
 * 拷贝资源的用例
 */
class ResourceUseCase {

//    /**
//     * 拷贝资源
//     * 直接将resources/libs目录中的所有文件拷贝到了本机上
//     * 没有做系统及架构的区分，可优化
//     */
//    fun copyLibsResource() {
//
//        // TODO 找到打包后本地的文件路径
//        val libsResourcesDir = File("")
//
//        // 创建本地libs文件并拷贝
//        val libsLocalDir = File(getLocalLibsDirPath())
//        createDirIfNoExists(libsLocalDir.absolutePath)
//
//        // 跳过已经拷贝过的文件
//        libsResourcesDir.walk().forEach {
//            logger("遍历文件：${it.absolutePath}")
//            val targetFile = File(libsLocalDir, it.path.substring(libsResourcesDir.path.length))
//            if (!targetFile.exists()) {
//                it.copyTo(targetFile)
//
//                //注意：千万别忘了给文件可执行的权限，否则可能会遇到错误 error=13, Permission denied
//                targetFile.setExecutable(true)
//                logger("拷贝文件：${it.name}")
//            }
//        }
//    }

    fun copyLibsToLocal() {

        val libsPrefix = "libs/"

        val libList = listOf(
            "${libsPrefix}${LIB_NAME_APK_TOOL}",
            "${libsPrefix}${LIB_NAME_DEX_2_JAR}",
            "${libsPrefix}${LIB_NAME_JD_GUI}",
            "${libsPrefix}${LIB_NAME_PROCYON}",
            "${libsPrefix}${LIB_NAME_AAPT2}",
        )

        libList.forEach {
            copyProjectResourcesToLocal(
                it, getLocalFilePathFromResourceFilePath(it)
            )
        }
    }

    /**
     * 拷贝文件，例如 libs/apktool_2.7.0.jar
     */
    @OptIn(ExperimentalComposeUiApi::class)
    private fun copyProjectResourcesToLocal(
        resourceFilePath: String,
        localFilePath: String
    ) {
        val localFile = File(localFilePath)
        if (localFile.exists()) {
            logger("本地资源文件已存在：${localFilePath}")
            return
        }

        createFileIfNoExists(localFilePath)

        ResourceLoader.Default.load(resourceFilePath)
            .use { inputStream ->

                val fos = FileOutputStream(localFile)
                val buffer = ByteArray(1024)
                var len: Int
                while (((inputStream.read(buffer).also { len = it })) != -1) {
                    fos.write(buffer, 0, len)
                }
                fos.flush()
                inputStream.close()
                fos.close()
            }

        //如果是压缩包，那么解压到本地
        val localFileName = localFile.name
        if (localFileName.endsWith(".zip")) {
            decompressByZip(
                zipFilePath = localFilePath,
                outputDirPath = localLibsDirPath()
                    + File.separator + localFileName.replace(".zip", "")
            )
        } else {
            //给予文件可执行权限
            localFile.setExecutable(true)
        }
    }

    /**
     * 根据resources中资源的路径，从本地创建相对一致的路径
     */
    private fun getLocalFilePathFromResourceFilePath(
        resourceFilePath: String
    ): String {

        val sb = StringBuilder(localWorkspaceDirPath())
        val splits = resourceFilePath.split("/").toMutableList()

        val fileName = splits.removeLast()

        // 构造文件目录
        splits.forEach {
            sb.append(File.separator).append(it)
        }

        // 构造文件
        sb.append(File.separator).append(fileName)
        return sb.toString()
    }
}