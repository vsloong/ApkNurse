import androidx.compose.material.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.ui.BottomBar
import com.vsloong.apknurse.ui.LeftBar
import com.vsloong.apknurse.ui.RightBar
import com.vsloong.apknurse.ui.TopBar
import com.vsloong.apknurse.ui.theme.appBackgroundColor
import com.vsloong.apknurse.usecase.*
import com.vsloong.apknurse.utils.ioScope
import kotlinx.coroutines.launch
import java.io.File

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
            TopBar(
                onMinClick = onMinClick,
                onMaxOrNormalClick = onMaxOrNormalClick,
                onExitClick = onExitClick,
                onWindowPositionChange = onWindowPositionChange
            )

            Row(modifier = Modifier.weight(1f)) {
                LeftBar()

                Column(modifier = Modifier.weight(1f)) {
                    AppTest()
                }

                RightBar()
            }

            BottomBar()
        }
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
        }
    }
}


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

fun main() = application {

    val windowState = rememberWindowState(
        placement = WindowPlacement.Floating,
        isMinimized = false,
        position = WindowPosition.PlatformDefault,
        size = DpSize(900.dp, 600.dp),
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
