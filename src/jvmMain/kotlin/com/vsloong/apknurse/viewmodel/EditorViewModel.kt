package com.vsloong.apknurse.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.vsloong.apknurse.bean.EditorType
import com.vsloong.apknurse.bean.FileItemInfo
import com.vsloong.apknurse.bean.action.EditorTitleTabAction
import com.vsloong.apknurse.bean.state.EditorTitleTabState
import com.vsloong.apknurse.utils.logger
import java.io.File

/**
 * 编辑器的ViewModel
 * 文本编辑器、图片编辑器
 */
class EditorViewModel {

    /**
     * 编辑器的类型
     */
    val editorType = mutableStateOf<EditorType>(EditorType.NONE)

    /**
     * 编辑器标题栏选项卡相关状态
     */
    val editorTitleTabState = mutableStateOf(EditorTitleTabState())

    /**
     * 编辑器标题栏选项卡点击的相关事件
     */
    val editorTitleTabAction = EditorTitleTabAction(
        onClick = {
            viewFile(it)
        },
        onCloseClick = {
            deleteTabItem(it)
        }
    )

    /**
     * 查看文件内容
     */
    fun viewFile(
        fileItemInfo: FileItemInfo,
    ) {

        val file = File(fileItemInfo.parent, fileItemInfo.name)
        val fileName = file.name

        if (fileName.endsWith(".java")
            || fileName.endsWith(".xml")
            || fileName.endsWith(".smali")
        ) {
            updateTabItem(
                fileItemInfo = fileItemInfo,
                editorType = EditorType.TEXT(
                    textContent = file.readText(charset = Charsets.UTF_8),
                    textType = file.extension
                )
            )
        } else if (fileName.endsWith(".png")
            || fileName.endsWith(".jpg")
            || fileName.endsWith(".jpeg")
            || fileName.endsWith(".webp")
        ) {
            updateTabItem(
                fileItemInfo = fileItemInfo,
                editorType = EditorType.IMAGE(imagePath = file.absolutePath)
            )
        }
    }

    /**
     * 删除Tab中的信息
     */
    private fun deleteTabItem(
        fileItemInfo: FileItemInfo
    ) {
        val viewedList = editorTitleTabState.value.viewedFileList.toMutableList()

        // 简单粗暴
        if (viewedList.size == 1) {
            logger("仅剩下最后一个TAB了，求求你别删了")
            return
        }

        val currentIndex = editorTitleTabState.value.selectedIndex
        val deleteIndex = viewedList.indexOf(fileItemInfo)

        // 删除该文件信息，并更新tab
        viewedList.remove(fileItemInfo)
        editorTitleTabState.value = EditorTitleTabState(
            selectedIndex = if (currentIndex > deleteIndex) {
                currentIndex - 1
            } else {
                currentIndex
            },
            viewedFileList = viewedList
        )

        // 如果删除的是当前正在预览的文件，那么需要更新预览内容
        if (currentIndex == deleteIndex) {
            val tabSize = viewedList.size

            if (tabSize == 0) {
                return
            }

            if (currentIndex < tabSize) {
                viewFile(viewedList[currentIndex])
            } else {
                viewFile(viewedList[tabSize - 1])
            }
        }
    }

    /**
     * 更新TAB栏状态
     */
    private fun updateTabItem(
        fileItemInfo: FileItemInfo,
        editorType: EditorType
    ) {

        val currentList = editorTitleTabState.value.viewedFileList.toMutableList()
        val index = currentList.indexOf(fileItemInfo)

        // 更改标题栏的内容
        if (index < 0) {
            currentList.add(fileItemInfo)
            editorTitleTabState.value = EditorTitleTabState(
                selectedIndex = currentList.size - 1,
                viewedFileList = currentList
            )
        } else {
            editorTitleTabState.value = editorTitleTabState.value.copy(
                selectedIndex = index
            )
        }

        // 更改编辑器中的内容
        this.editorType.value = editorType
    }
}