package com.vsloong.apknurse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.vsloong.apknurse.ui.theme.appBarColor
import com.vsloong.apknurse.ui.theme.appBarSize
import com.vsloong.apknurse.ui.theme.appBottomBarSize
import com.vsloong.apknurse.ui.theme.appTopBarColor

@Composable
fun TopBar(
    onMinClick: () -> Unit,
    onMaxOrNormalClick: () -> Unit,
    onExitClick: () -> Unit,
    onWindowPositionChange: (Float, Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(appBarSize)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onWindowPositionChange(dragAmount.x, dragAmount.y)
                }
            }
            .background(color = appTopBarColor)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BarMenu(iconPath = "icons/app_icon.png") {

        }

        Spacer(modifier = Modifier.weight(1f))

        BarMenu(
            iconPath = "icons/bar_top_menu_min.svg",
            onClick = onMinClick
        )

        BarMenu(
            iconPath = "icons/bar_top_menu_max.svg",
            onClick = onMaxOrNormalClick
        )

        BarMenu(
            iconPath = "icons/bar_top_menu_close.svg",
            onClick = onExitClick
        )
    }
}

@Composable
fun BottomBar() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(appBottomBarSize)
            .background(color = appBarColor)
    ) {

    }
}

@Composable
fun LeftBar(
    onFolderClick: () -> Unit = {},
    folderSelected: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxHeight()
            .width(appBarSize)
            .background(color = appBarColor)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BarMenu(
            iconPath = "icons/bar_left_folder.svg",
            selected = folderSelected,
            onClick = onFolderClick,
        )
    }
}

@Composable
fun RightBar(

) {
    Column(
        modifier = Modifier.fillMaxHeight()
            .width(appBarSize)
            .background(color = appBarColor)
    ) {

    }
}