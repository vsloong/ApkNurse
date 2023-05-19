package com.vsloong.apknurse.manager

import com.vsloong.apknurse.bean.ApkBasicInfo
import com.vsloong.apknurse.bean.ApkNurseInfo
import com.vsloong.apknurse.bean.EditorType
import com.vsloong.apknurse.bean.FileItemInfo
import com.vsloong.apknurse.viewmodel.DragViewModel
import com.vsloong.apknurse.viewmodel.EditorViewModel
import com.vsloong.apknurse.viewmodel.LeftBarViewModel
import com.vsloong.apknurse.viewmodel.ProjectPanelViewModel
import java.io.File

/**
 * 工程的单例处理类
 */
object NurseManager {

    val leftBarViewModel = LeftBarViewModel()
    val dragViewModel = DragViewModel()
    val projectPanelViewModel = ProjectPanelViewModel()
    val editorViewModel = EditorViewModel()

    private var apkNurseInfo = ApkNurseInfo()

    fun updateApkNurseInfo(apkBasicInfo: ApkBasicInfo) {
        apkNurseInfo = ApkNurseInfo(apkBasicInfo)
    }

    fun getApkNurseInfo(): ApkNurseInfo {
        return apkNurseInfo
    }

    /**
     * 创建工程
     */
    fun createProject(file: File) {
        projectPanelViewModel.createProject(file)
        leftBarViewModel.openProject()
    }

    /**
     * 查看文件
     */
    fun viewFile(fileItemInfo: FileItemInfo) {
        editorViewModel.viewFile(fileItemInfo)
    }

}