package com.vsloong.apknurse.viewmodel

import com.vsloong.apknurse.bean.action.DragAction
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.usecase.ApkUseCase
import com.vsloong.apknurse.usecase.DexUseCase
import com.vsloong.apknurse.usecase.HackUseCase
import com.vsloong.apknurse.usecase.Jar2JavaUseCase
import com.vsloong.apknurse.utils.ioScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * 拖拽文件处理的相关ViewModel
 */
class DragViewModel {

    val dragAction = DragAction(
        onFileDrop = {
            onDragFile(it)
        }
    )

    /**
     * 拖拽的文件
     */
    private fun onDragFile(files: List<File>) {

        val apk = files.firstOrNull() {
            it.name.endsWith(".apk")
        } ?: return

        ioScope.launch {

            val apkFilePath = apk.absolutePath

            val apkUseCase = ApkUseCase()
            val apkInfo = apkUseCase.getApkInfo(apkFilePath)
            NurseManager.updateApkNurseInfo(apkInfo)

            // 解压APK
            apkUseCase.decompressApk(
                apkFilePath = apkFilePath,
                outputDirPath = NurseManager.getApkNurseInfo().getDecompressDirPath()
            )

            // 反编译dex文件
            val dexUseCase = DexUseCase()
            dexUseCase.dex2jar(
                dexPath = NurseManager.getApkNurseInfo().getDecompressDirPath(),
                outDirPath = NurseManager.getApkNurseInfo().getDecompiledJarDirPath()
            )

            // 反编译jar文件
            val jar2JavaUseCase = Jar2JavaUseCase()
            jar2JavaUseCase.jar2java(
                jarPath = NurseManager.getApkNurseInfo().getDecompiledJarDirPath(),
                outDirPath = NurseManager.getApkNurseInfo().getDecompiledJavaDirPath()
            )

            // 解码APK
            val hackUseCase = HackUseCase()
            hackUseCase.decodeApk(
                apkPath = apkFilePath,
                decodeDirPath = NurseManager.getApkNurseInfo().getDecodeDirPath()
            )

            // 生成项目目录
            NurseManager.createProject(
                File(NurseManager.getApkNurseInfo().getCurrentProjectDirPath())
            )
        }
    }
}