package com.vsloong.apknurse.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.vsloong.apknurse.bean.FileItemInfo
import com.vsloong.apknurse.bean.ProjectTreeType
import com.vsloong.apknurse.bean.action.ProjectPanelAction
import com.vsloong.apknurse.bean.state.ProjectPanelState
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.utils.logger
import java.io.File


/**
 * 项目工程目录显示区域处理
 */
class ProjectPanelViewModel {

    /**
     * 工程目录的跟地址
     */
    private var projectDirPath = ""

    /**
     * 工程文件信息
     */
    private val projectTreeList = mutableListOf<FileItemInfo>()
    private val cachedProjectTreeList = mutableListOf<FileItemInfo>()
    private val packagesTreeList = mutableListOf<FileItemInfo>()
    private val cachedPackagesTreeList = mutableListOf<FileItemInfo>()

    /**
     * 工程树中的各种状态
     */
    val projectPanelState = mutableStateOf(ProjectPanelState())

    /**
     * 点击事件处理
     */
    val projectPanelAction = ProjectPanelAction(
        onFileItemClick = {
            clickFileItem(it)
        },
        onProjectTreeTypeClick = { oldType ->
            if (oldType == ProjectTreeType.PROJECT) {
                createPackagesTree()
            } else {
                createProjectTree()
            }
        }
    )

    /**
     * 创建项目结构树
     * ProjectTree模式，对应文件结构
     */
    private fun createProjectTree() {

        if (projectTreeList.isEmpty()) {
            // 把所有的文件遍历出来
            val dir = File(projectDirPath)

            dir.walk()
                .filter {
                    it.name != ".DS_Store"
                }
                .forEachIndexed { index, file ->
                    logger("each -> ${file.name}")
                    projectTreeList.add(
                        FileItemInfo(
                            parent = file.parent,
                            name = file.name,
                            isDir = file.isDirectory,
                            isRootFile = index == 0
                        )
                    )
                }
        }

        // 缓存packages模式的tree
        cachedPackagesTreeList.clear()
        cachedPackagesTreeList.addAll(projectPanelState.value.showedTreeList)


        // 切换为project模式
        projectPanelState.value = ProjectPanelState(
            projectTreeType = ProjectTreeType.PROJECT,
            showedTreeList = if (cachedProjectTreeList.isEmpty()) {
                projectTreeList.filter {
                    it.isRootFile
                }
            } else {
                cachedProjectTreeList.toList()
            }
        )
    }

    /**
     * 创建项目结构树
     * PackagesTree模式，仅包含资源文件，反编译后java文件
     */
    private fun createPackagesTree() {
        if (packagesTreeList.isEmpty()) {
            fun walkFile(file: File) {
                if (file.exists()) {
                    val list = mutableListOf<FileItemInfo>()
                    file.walk()
                        .filter {
                            it.name != ".DS_Store"
                        }
                        .forEachIndexed { index, file ->
                            logger("each -> ${file.name}")
                            list.add(
                                FileItemInfo(
                                    parent = file.parent,
                                    name = file.name,
                                    showName = if (file.name == "decompiled_java") {
                                        "java"
                                    } else {
                                        ""
                                    },
                                    isDir = file.isDirectory,
                                    isRootFile = index == 0
                                )
                            )
                        }
                    packagesTreeList.addAll(list)
                }
            }

            // 反编译后的java文件夹
            val javaDir = File(projectDirPath, "decompiled_java")
            walkFile(javaDir)

            // 解码后的资源文件夹
            val decodeRes = File(projectDirPath, "decode/res")
            walkFile(decodeRes)

            // 解码后的smali代码文件夹
            val decodeSmali = File(projectDirPath, "decode/smali")
            walkFile(decodeSmali)
        }

        // 缓存project模式的tree
        cachedProjectTreeList.clear()
        cachedProjectTreeList.addAll(projectPanelState.value.showedTreeList)


        // 切换为packages模式
        projectPanelState.value = ProjectPanelState(
            projectTreeType = ProjectTreeType.PACKAGES,
            showedTreeList = if (cachedPackagesTreeList.isEmpty()) {
                packagesTreeList.filter {
                    it.isRootFile
                }
            } else {
                cachedPackagesTreeList.toList()
            }
        )
    }

    /**
     * 双击了文件夹或者文件
     */
    private fun clickFileItem(item: FileItemInfo) {

        val fileTreeList = mutableListOf<FileItemInfo>()
        fileTreeList.addAll(projectPanelState.value.showedTreeList)

        // 如果是文件夹
        if (item.isDir) {

            // 选中的文件夹的地址
            val currentPath = File(item.parent, item.name).absolutePath

            // 如果当前文件夹已经被选中了，那么本次需要折叠
            if (item.selected) {
                // 该项的位置
                val index = fileTreeList.indexOf(item)

                fileTreeList[index] = fileTreeList[index].copy(selected = false)
                fileTreeList.removeIf {

                    // 所有以该地址开头的都要折叠
                    it.parent.startsWith(currentPath)
                }
            }
            // 如果当前文件夹未被选中，那么本次需要展开
            else {
                val listFilter = projectTreeList
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
                val index = fileTreeList.indexOf(item)

                // 选中当前文件夹，并展开
                fileTreeList[index] = fileTreeList[index].copy(selected = true)
                fileTreeList.addAll(index + 1, listFilter)
            }

            // 更新状态
            projectPanelState.value = projectPanelState.value.copy(
                showedTreeList = fileTreeList
            )
        }
        // 如果是文件
        else {
            createCodeEditString(item)
        }
    }

    /**
     * 获取文件的字符串信息
     */
    private fun createCodeEditString(item: FileItemInfo) {
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
            NurseManager.showCode(codeFile.readText(charset = Charsets.UTF_8))
        }
    }

    /**
     * 创建工程
     * 默认ProjectTree模式
     */
    fun createProject(file: File) {
        projectDirPath = file.absolutePath
        createProjectTree()
    }
}