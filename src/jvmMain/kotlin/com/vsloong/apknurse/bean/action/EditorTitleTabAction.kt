package com.vsloong.apknurse.bean.action

import androidx.compose.runtime.Stable
import com.vsloong.apknurse.bean.FileItemInfo

/**
 * 编辑器标题栏的相关事件
 */
@Stable
data class EditorTitleTabAction(
    val onClick: (FileItemInfo) -> Unit,
    val onCloseClick: (FileItemInfo) -> Unit
)
