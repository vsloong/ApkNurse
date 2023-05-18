package com.vsloong.apknurse.ui.panel

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(
        modifier = modifier,
    ) {
        val file = File(content)
        val imageBitmap: ImageBitmap = loadImageBitmap(file.inputStream())

        val zoomTimes = remember {
            mutableStateOf(1f)
        }

        Row(
            modifier = Modifier.height(30.dp)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource("icons/icon_zoom_in.svg"),
                contentDescription = "",
                modifier = Modifier.size(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        val tempZoom = zoomTimes.value + 0.3f
                        if (tempZoom < 5) {
                            zoomTimes.value = tempZoom
                        }
                    }
            )

            Image(
                painter = painterResource("icons/icon_zoom_out.svg"),
                contentDescription = "",
                modifier = Modifier.size(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        val tempZoom = zoomTimes.value - 0.3f
                        if (tempZoom > 0f) {
                            zoomTimes.value = tempZoom
                        }
                    }
            )

            Spacer(modifier = Modifier.fillMaxHeight().weight(1f))

            Text(
                text = "${imageBitmap.width}x${imageBitmap.height} " +
                    "${file.extension.uppercase()} " +
                    "(${imageBitmap.colorSpace.name}) " +
                    "${"%.2f".format(file.length() * 1f / 1024)}KB",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // 加载本地的图片，非工程图片
            Image(
                bitmap = imageBitmap,
                contentDescription = "",
                modifier = Modifier.scale(zoomTimes.value)
            )
        }


    }

}