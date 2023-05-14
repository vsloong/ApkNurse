import androidx.compose.material.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.vsloong.apknurse.ui.BottomBar
import com.vsloong.apknurse.ui.LeftBar
import com.vsloong.apknurse.ui.RightBar
import com.vsloong.apknurse.ui.TopBar
import com.vsloong.apknurse.ui.theme.appBackgroundColor
import com.vsloong.apknurse.usecase.*
import com.vsloong.apknurse.utils.fileTimeMillis
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
                .border(width = 1.dp, color = Color(0xff393b40))
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
                        val apkFilePath = "D:\\ApkNurse\\app-debug.apk"

                        val useCase = ApkUseCase()
                        val apkInfo = useCase.getApkInfo(apkFilePath)

                        val outDirName = apkInfo.packageName +
                            "_${apkInfo.versionCode}" +
                            "_${apkInfo.versionName}" +
                            "_${fileTimeMillis()}"

                        useCase.decompressApk(
                            apkFilePath = apkFilePath,
                            outputDirPath = localProjectsDirPath()
                                + File.separator + outDirName
                        )
                    }
                }
            )

            MyButton(
                text = "Dex文件转Jar文件",
                onClick = {
                    ioScope.launch {
                        val dexFilePath =
                            "D:\\ApkNurse\\projects\\com.example.recompositionsamle_1_1.0_2023_05_14_14_45_29_832\\classes4.dex"

                        val outJarFilePath =
                            "D:\\ApkNurse\\projects\\classes4.dex.jar"

                        val dexUseCase = DexUseCase()
                        dexUseCase.dex2jar(
                            dexFilePath = dexFilePath,
                            outJarFilePath = outJarFilePath
                        )
                    }
                }
            )

            MyButton(
                text = "使用JD-GUi查看Jar文件",
                onClick = {
                    ioScope.launch {

                        val outJarFilePath =
                            "D:\\ApkNurse\\projects\\classes4.dex.jar"

                        val jdUseCase = JavaDecompilerUseCase()
                        jdUseCase.viewJar(
                            jarFilePath = outJarFilePath
                        )
                    }
                }
            )

            MyButton(
                text = "Jar文件反编译为Java文件",
                onClick = {
                    ioScope.launch {
                        val outJarFilePath =
                            "D:\\ApkNurse\\projects\\classes4.dex.jar"

                        val outJavaDir = "D:\\ApkNurse\\projects\\java"

                        val procyonUseCase = ProcyonUseCase()
                        procyonUseCase.decompile(
                            jarFilePath = outJarFilePath,
                            outDirPath = outJavaDir
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
