package com.vsloong.apknurse.bean.action

import java.io.File

/**
 * 拖拽文件释放的行为
 */
data class DragAction(
    val onFileDrop: (List<File>) -> Unit
)