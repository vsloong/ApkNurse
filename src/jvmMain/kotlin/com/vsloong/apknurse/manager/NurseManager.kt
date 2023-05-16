package com.vsloong.apknurse.manager

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.vsloong.apknurse.bean.ApkBasicInfo
import com.vsloong.apknurse.bean.ApkNurseInfo
import com.vsloong.apknurse.bean.FolderItemInfo
import com.vsloong.apknurse.usecase.ApkUseCase
import com.vsloong.apknurse.usecase.DexUseCase
import com.vsloong.apknurse.usecase.HackUseCase
import com.vsloong.apknurse.usecase.Jar2JavaUseCase
import com.vsloong.apknurse.utils.ioScope
import com.vsloong.apknurse.utils.logger
import kotlinx.coroutines.launch
import java.io.File

object NurseManager {

    /**
     * 是否展示Project Panel
     */
    val showProjectPanel = mutableStateOf(false)


    /**
     * 工程文件夹处理
     */
    private val allFolderList = mutableListOf<FolderItemInfo>()
    val showFolderList = mutableStateListOf<FolderItemInfo>()

    /**
     * 代码编辑区域
     */
    val codeEditContent = mutableStateOf("")


    private val apkNurseInfo = mutableStateOf(ApkNurseInfo())

    fun updateApkNurseInfo(apkBasicInfo: ApkBasicInfo) {
        apkNurseInfo.value = ApkNurseInfo(apkBasicInfo)
    }

    fun getApkNurseInfo(): ApkNurseInfo {
        return apkNurseInfo.value
    }


    /**
     * 创建项目结构树
     */
    fun createProjectTree(dir: File) {

        val list = mutableListOf<FolderItemInfo>()
        dir.walk()
            .onEnter {
                logger("onEnter -> ${it.name}")
                it.isDirectory
            }
            .onLeave {
                logger("onLeave -> ${it.name}")
            }
            .filter {
                it.name != ".DS_Store"
            }
            .forEach {
                logger("each -> ${it.name}")
                list.add(
                    FolderItemInfo(
                        parent = it.parent,
                        name = it.name,
                        isDir = it.isDirectory
                    )
                )
            }

        list.forEach {
            logger("文件遍历结果：${it.name}")
        }

        allFolderList.addAll(list)

        // 将第一个参数传递进去
        val rootDir = allFolderList.first()
        showFolderList.add(rootDir)
    }

    /**
     * 双击了文件夹或者文件
     */
    fun clickFolderItem(item: FolderItemInfo) {
        // 如果是文件夹
        if (item.isDir) {

            // 选中的文件夹的地址
            val currentPath = File(item.parent, item.name).absolutePath

            // 如果当前文件夹已经被选中了，那么本次需要折叠
            if (item.selected) {
                // 该项的位置
                val index = showFolderList.indexOf(item)

                showFolderList[index] = showFolderList[index].copy(selected = false)
                showFolderList.removeIf {

                    // 所有以该地址开头的都要折叠
                    it.parent.startsWith(currentPath)
                }
            }
            // 如果当前文件夹未被选中，那么本次需要展开
            else {
                val listFilter = allFolderList
                    .filter {
                        it.parent == currentPath
                    }
                    .sortedWith { u1, u2 ->
                        // 如果是同类型的，那么直接排序
                        if (u1.isDir == u2.isDir) {
                            u1.name.compareTo(u2.name)
                        } else {
                            u2.isDir.compareTo(u1.isDir)
                        }
                    }
                    .map {
                        val depth = item.depth + 1
                        it.apply {
                            this.depth = depth
                        }
                    }

                // 插入的位置
                val index = showFolderList.indexOf(item)

                // 选中当前文件夹，并展开
                showFolderList[index] = showFolderList[index].copy(selected = true)
                showFolderList.addAll(index + 1, listFilter)
            }
        }

        // 如果是文件
        else {
            createCodeEditString(item)
        }
    }

    /**
     * 获取文件的字符串信息
     */
    private fun createCodeEditString(item: FolderItemInfo) {
        val codeFile = File(item.parent, item.name)

        if (!codeFile.isFile) {
            logger("Not a file : ${codeFile.absolutePath}")
            return
        }

        val fileName = codeFile.name

        if (fileName.endsWith(".java") ||
            fileName.endsWith(".xml") ||
            fileName.endsWith(".smali")
        ) {
            codeEditContent.value = codeFile.readText(charset = Charsets.UTF_8)
        }
    }

    /**
     * 拖拽的文件
     */
    fun onDragFile(files: List<File>) {

        val apk = files.firstOrNull() {
            it.name.endsWith(".apk")
        } ?: return

        ioScope.launch {

            val apkFilePath = apk.absolutePath

            val apkUseCase = ApkUseCase()
            val apkInfo = apkUseCase.getApkInfo(apkFilePath)
            updateApkNurseInfo(apkInfo)

            // 解压APK
            apkUseCase.decompressApk(
                apkFilePath = apkFilePath,
                outputDirPath = getApkNurseInfo().getDecompressDirPath()
            )

            // 反编译dex文件
            val dexUseCase = DexUseCase()
            dexUseCase.dex2jar(
                dexPath = getApkNurseInfo().getDecompressDirPath(),
                outDirPath = getApkNurseInfo().getDecompiledJarDirPath()
            )

            // 反编译jar文件
            val jar2JavaUseCase = Jar2JavaUseCase()
            jar2JavaUseCase.jar2java(
                jarPath = getApkNurseInfo().getDecompiledJarDirPath(),
                outDirPath = getApkNurseInfo().getDecompiledJavaDirPath()
            )

            // 解码APK
            val hackUseCase = HackUseCase()
            hackUseCase.decodeApk(
                apkPath = apkFilePath,
                decodeDirPath = getApkNurseInfo().getDecodeDirPath()
            )

            // 生成项目目录
            createProjectTree(
                File(getApkNurseInfo().getCurrentProjectDirPath())
            )

        }
    }
}