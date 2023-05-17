package com.vsloong.apknurse.manager

import com.vsloong.apknurse.bean.ApkBasicInfo
import com.vsloong.apknurse.bean.ApkNurseInfo
import com.vsloong.apknurse.bean.EditorType
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
     * 点击源文件后打开代码内容
     */
    fun showCode(srcCode: String) {
        editorViewModel.editorType.value = EditorType.TEXT(codeString = srcCode)
    }

    /**
     * 展示图片
     */
    fun showImage(imagePath: String) {
        editorViewModel.editorType.value = EditorType.IMAGE(imagePath = imagePath)
    }

}