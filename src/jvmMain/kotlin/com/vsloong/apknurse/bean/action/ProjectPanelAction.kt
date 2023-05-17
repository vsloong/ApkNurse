package com.vsloong.apknurse.bean.action

import androidx.compose.runtime.Stable
import com.vsloong.apknurse.bean.FileItemInfo
import com.vsloong.apknurse.bean.ProjectTreeType

/**
 * 工程目录面板行为
 */
@Stable
data class ProjectPanelAction(
    val onFileItemClick: (FileItemInfo) -> Unit,
    val onProjectTreeTypeClick: (ProjectTreeType) -> Unit
)
