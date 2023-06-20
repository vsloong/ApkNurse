import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.vsloong.apknurse.ui.theme.appBackgroundColor

fun main() = run {

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
            state = windowState
        ) {

//            val horizontalScrollState = rememberScrollState()
//            val verticalScrollState = rememberScrollState()
//
//            val editContent = File(
//                "/Users/dragon/ApkNurse" +
//                    "/projects/com.psd.live_2770_2023_05_19_10_46_20_051/decompiled_jar" +
//                    "/sources/com/psd/appuser/activity/level/NobleLevelActivity.java"
//            )
//                .readText(charset = Charsets.UTF_8)
//
//            val compilationUnit = StaticJavaParser.parse(editContent)
//            compilationUnit.findAll(MethodDeclaration::class.java)
//                .forEach { methodDeclaration ->
//                    logger("方法名是：${methodDeclaration.name}")
//                }

            val projectDirPath =
                "/Users/dragon/ApkNurse" +
                    "/projects/com.doupaocity_2791_2023_06_20_11_04_58_822"


            Column(
                modifier = Modifier.fillMaxSize().background(color = appBackgroundColor)
            ) {

                Column(
                    modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)
                        .padding(16.dp)
                ) {

                    MyButton(
                        text = "生成xml文件",
                    ) {

                    }
                }

            }
        }
    }
}

@Composable
private fun MyButton(
    text: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = text, fontSize = 16.sp)
    }
}