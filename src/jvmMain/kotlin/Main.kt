import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.vsloong.apknurse.bean.EditorType
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.viewmodel.LeftBarViewModel
import com.vsloong.apknurse.ui.*
import com.vsloong.apknurse.ui.drag.DropHerePanel
import com.vsloong.apknurse.ui.panel.EditPanel
import com.vsloong.apknurse.ui.panel.ImagePanel
import com.vsloong.apknurse.ui.panel.ProjectPanel
import com.vsloong.apknurse.ui.theme.appBackgroundColor
import com.vsloong.apknurse.viewmodel.DragViewModel
import com.vsloong.apknurse.viewmodel.EditorViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {

    factory {

    }
}

fun main() = run {
    startKoin {
        modules(appModule)
    }

    application {

        val windowState = rememberWindowState(
            placement = WindowPlacement.Floating,
            isMinimized = false,
            position = WindowPosition.PlatformDefault,
            size = DpSize(1440.dp, 900.dp),
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "",
            icon = painterResource("icons/app_icon.svg"),
            transparent = true,
            undecorated = true,
            state = windowState
        ) {
            AppFrame(
                composeWindow = window,
                onMinClick = {
                    windowState.isMinimized = true
                },
                onMaxOrNormalClick = {
                    windowState.isMinimized = false
                },
                onExitClick = {
                    exitApplication()
                },
                onWindowPositionChange = { offsetX, offsetY ->
                    windowState.position = WindowPosition(
                        x = windowState.position.x + offsetX.dp,
                        y = windowState.position.y + offsetY.dp,
                    )
                }
            )
        }
    }
}

/**
 * 将应用的UI规划为几个大部分
 */
@Composable
fun AppFrame(
    composeWindow: ComposeWindow,
    onMinClick: () -> Unit,
    onMaxOrNormalClick: () -> Unit,
    onExitClick: () -> Unit,
    onWindowPositionChange: (Float, Float) -> Unit,
    leftBarViewModel: LeftBarViewModel = NurseManager.leftBarViewModel,
    dragViewModel: DragViewModel = NurseManager.dragViewModel,
    editorViewModel: EditorViewModel = NurseManager.editorViewModel
) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(width = 2.dp, color = Color(0xff393b40), shape = RoundedCornerShape(8.dp))
                .background(color = appBackgroundColor),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            // 顶部功能栏
            TopBar(
                onMinClick = onMinClick,
                onMaxOrNormalClick = onMaxOrNormalClick,
                onExitClick = onExitClick,
                onWindowPositionChange = onWindowPositionChange
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {

                // 左侧功能栏
                LeftBar(
                    leftBarAction = leftBarViewModel.leftBarAction,
                    leftBarState = leftBarViewModel.leftBarState.value
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {

                        // 工程项目结构区域
                        if (leftBarViewModel.leftBarState.value.projectOn) {
                            ProjectPanel(modifier = Modifier.width(320.dp))
                        }

                        when (editorViewModel.editorType.value) {
                            is EditorType.NONE -> {
                                // 拖动文件到此
                                DropHerePanel(
                                    modifier = Modifier.fillMaxSize()
                                        .weight(1f),
                                    composeWindow = composeWindow,
                                    onFileDrop = {
                                        dragViewModel.dragAction.onFileDrop
                                    }
                                )
                            }
                            is EditorType.TEXT -> {
                                // 代码编辑区域
                                EditPanel(
                                    modifier = Modifier.fillMaxSize()
                                        .weight(1f)
                                        .background(color = appBackgroundColor)
                                        .padding(2.dp),
                                    editContent = (editorViewModel.editorType.value as EditorType.TEXT).codeString
                                )
                            }
                            is EditorType.IMAGE -> {
                                // 图片编辑区域
                                ImagePanel(
                                    modifier = Modifier.fillMaxSize()
                                        .weight(1f)
                                        .background(color = appBackgroundColor)
                                        .padding(2.dp),
                                    content = (editorViewModel.editorType.value as EditorType.IMAGE).imagePath
                                )
                            }
                        }
                    }
                    // 底部的控制台区域


                }

                // 右侧功能栏
                RightBar()
            }

            // 底部功能栏
            BottomBar()
        }
    }
}
