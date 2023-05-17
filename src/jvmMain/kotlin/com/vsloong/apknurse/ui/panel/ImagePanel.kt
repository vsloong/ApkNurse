package com.vsloong.apknurse.ui.panel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import java.io.File

/**
 * 预览图片的控制面板
 * 支持png、jpg、webp等
 */
@Composable
fun ImagePanel(
    modifier: Modifier,
    content: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        val file = File(content)
        val imageBitmap = loadImageBitmap(file.inputStream())

        // 加载本地的图片，非工程图片
        Image(
            bitmap = imageBitmap,
            contentDescription = ""
        )
    }

}