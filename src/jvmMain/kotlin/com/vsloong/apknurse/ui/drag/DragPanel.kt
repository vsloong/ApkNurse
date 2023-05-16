package com.vsloong.apknurse.ui.drag

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.sp
import com.vsloong.apknurse.ui.theme.textColor
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import kotlin.math.roundToInt

/**
 * 可拖拽文件到此的面板
 */

@Composable
fun DropHerePanel(
    modifier: Modifier,
    composeWindow: ComposeWindow,
    onFileDrop: (List<File>) -> Unit
) {

    val component = remember {
        ComposePanel().apply {
            val target = object : DropTarget() {
                override fun drop(event: DropTargetDropEvent) {
                    event.acceptDrop(DnDConstants.ACTION_REFERENCE)
                    val dataFlavors = event.transferable.transferDataFlavors
                    dataFlavors.forEach {
                        if (it == DataFlavor.javaFileListFlavor) {
                            val list = event.transferable.getTransferData(it) as List<*>
                            list.map { filePath ->
                                File(filePath.toString())
                            }.also(onFileDrop)
                        }
                    }
                    event.dropComplete(true)
                }
            }
            dropTarget = target
            isOpaque = false
        }
    }

    val pane = remember {
        composeWindow.rootPane
    }

    Box(
        modifier = modifier
            .onPlaced {
                val x = it.positionInWindow().x.roundToInt()
                val y = it.positionInWindow().y.roundToInt()
                val width = it.size.width
                val height = it.size.height
                component.setBounds(x, y, width, height)
            },
        contentAlignment = Alignment.Center
    ) {

        Text(text = "请拖拽文件到这里哦", fontSize = 36.sp, color = textColor)

        DisposableEffect(true) {
            pane.add(component)
            onDispose {
                pane.remove(component)
            }
        }
    }
}
