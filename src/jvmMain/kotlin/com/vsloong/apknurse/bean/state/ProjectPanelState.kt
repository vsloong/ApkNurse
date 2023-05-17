package com.vsloong.apknurse.bean.state

import androidx.compose.runtime.Stable
import com.vsloong.apknurse.bean.FileItemInfo
import com.vsloong.apknurse.bean.ProjectTreeType

@Stable
data class ProjectPanelState(
    var projectTreeType: ProjectTreeType = ProjectTreeType.PROJECT(),
    var showedTreeList: List<FileItemInfo> = mutableListOf()
)