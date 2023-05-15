package com.vsloong.apknurse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.usecase.ApkUseCase
import com.vsloong.apknurse.usecase.DexUseCase
import com.vsloong.apknurse.usecase.Jar2JavaUseCase
import com.vsloong.apknurse.usecase.ResourceUseCase
import com.vsloong.apknurse.utils.ioScope
import kotlinx.coroutines.launch
import localTempDirPath
import java.io.File


@Composable
private fun MyButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
    ) {
        Text(text)
    }
}

@Composable
fun AppTest() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xff1e1f22))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            MyButton(
                text = "拷贝资源文件到本机",
                onClick = {
                    ioScope.launch {
                        val resourceUseCase = ResourceUseCase()
                        resourceUseCase.copyLibsToLocal()
                    }
                }
            )

            MyButton(
                text = "解压APK文件",
                onClick = {
                    ioScope.launch {
                        val apkFilePath = localTempDirPath() + File.separator + "app-release-unsigned.apk"

                        val useCase = ApkUseCase()
                        val apkInfo = useCase.getApkInfo(apkFilePath)

                        NurseManager.updateApkNurseInfo(apkInfo)

                        useCase.decompressApk(
                            apkFilePath = apkFilePath,
                            outputDirPath = NurseManager.getApkNurseInfo().getDecompressDirPath()
                        )
                    }
                }
            )

            MyButton(
                text = "Dex文件转Jar文件",
                onClick = {
                    ioScope.launch {

                        val dexPath = NurseManager.getApkNurseInfo().getDecompressDirPath()
                        val outDirPath = NurseManager.getApkNurseInfo().getDecompileDirPath()

                        val dexUseCase = DexUseCase()
                        dexUseCase.dex2jar(
                            dexPath = dexPath,
                            outDirPath = outDirPath
                        )
                    }
                }
            )

            MyButton(
                text = "Jar文件反编译为Java文件",
                onClick = {
                    ioScope.launch {
                        val outJarPath = NurseManager.getApkNurseInfo().getDecompileDirPath()

                        val outJavaDirPath = NurseManager.getApkNurseInfo().getDecompileDirPath()

                        val jar2JavaUseCase = Jar2JavaUseCase()
                        jar2JavaUseCase.jar2java(
                            jarPath = outJarPath,
                            outDirPath = outJavaDirPath
                        )
                    }
                }
            )

            MyButton(
                text = "生成项目结构树",
                onClick = {
                    ioScope.launch {

                        NurseManager.createProjectTree()
                    }
                }
            )
        }
    }
}
