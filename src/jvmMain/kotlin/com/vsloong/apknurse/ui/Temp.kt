package com.vsloong.apknurse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.vsloong.apknurse.ui.theme.randomComposeColor
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
            .height(28.dp)
            .clip(RoundedCornerShape(50))
            .border(width = 2.dp, color = randomComposeColor(), shape = RoundedCornerShape(50))
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
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
            text = "生成项目结构",
            onClick = {
                ioScope.launch {
                    NurseManager.createProjectTree(
                        File("/Users/dragon/ApkNurse/projects/com.example.composesample_1_2023_05_16_20_43_18_608")
                    )
                }
            }
        )
    }

}
