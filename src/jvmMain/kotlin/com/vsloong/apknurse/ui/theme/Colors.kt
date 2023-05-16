package com.vsloong.apknurse.ui.theme

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * 生成随机颜色，方便查看重组情况
 */
fun randomComposeColor(): Color {
    return Color(
        red = Random.nextInt(256),
        green = Random.nextInt(256),
        blue = Random.nextInt(256),
        alpha = 255
    )
}

val appBackgroundColor = Color(0xff1e1f22)
val appTopBarColor = Color(0xff3c3f41)
val appBarColor = Color(0xff2b2d30)

val textColor = Color(0xffdfe1e5)

val codeTextColor = Color(0xFFBCBEC4)