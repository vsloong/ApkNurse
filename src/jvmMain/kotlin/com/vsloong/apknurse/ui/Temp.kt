package com.vsloong.apknurse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.ui.theme.textColor
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
    Box(
        modifier = Modifier.wrapContentWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(50))
            .background(color = Color.Blue)
            .padding(horizontal = 16.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp
        )
    }
}

@Composable
fun AppTest() {
    MaterialTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
