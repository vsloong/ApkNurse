import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.ui.*
import com.vsloong.apknurse.ui.panel.ProjectPanel
import com.vsloong.apknurse.ui.theme.appBackgroundColor


fun main() = application {

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


/**
 * 将应用的UI规划为几个大部分
 */
@Composable
fun AppFrame(
    onMinClick: () -> Unit,
    onMaxOrNormalClick: () -> Unit,
    onExitClick: () -> Unit,
    onWindowPositionChange: (Float, Float) -> Unit
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
                    folderSelected = NurseManager.showProjectPanel.value,
                    onFolderClick = {
                        NurseManager.showProjectPanel.value = !NurseManager.showProjectPanel.value
                    }
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
                        if (NurseManager.showProjectPanel.value) {
                            ProjectPanel(modifier = Modifier.width(320.dp))
                        }

                        // 文件编辑区域
                        AppTest()
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
