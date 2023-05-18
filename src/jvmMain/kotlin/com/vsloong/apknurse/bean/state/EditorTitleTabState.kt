package com.vsloong.apknurse.bean.state

import androidx.compose.runtime.Stable
import com.vsloong.apknurse.bean.FileItemInfo

/**
 * 编辑器标题栏的状态
 */
@Stable
data class EditorTitleTabState(
    val selectedIndex: Int = 0,
    val viewedFileList: List<FileItemInfo> = mutableListOf()
)
