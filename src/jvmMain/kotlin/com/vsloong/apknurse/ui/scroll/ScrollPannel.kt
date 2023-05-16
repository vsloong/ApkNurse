package com.vsloong.apknurse.ui.scroll

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 带有滚动条的面板
 * 支持横向滚动条，竖向滚动条
 */

@Composable
fun ScrollPanel(
    modifier: Modifier,
    verticalScrollStateAdapter: ScrollbarAdapter,
    horizontalScrollStateAdapter: ScrollbarAdapter,
    content: @Composable BoxScope.() -> Unit
) {

    Row(modifier = modifier) {

        Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                content()
            }

            HorizontalScrollbar(
                adapter = horizontalScrollStateAdapter,
                style = ScrollbarStyle(
                    minimalHeight = 16.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4.dp),
                    hoverDurationMillis = 300,
                    unhoverColor = Color.White.copy(alpha = 0.20f),
                    hoverColor = Color.White.copy(alpha = 0.50f)
                ),
                modifier = Modifier.fillMaxWidth().background(color = Color.Transparent)
            )
        }

        VerticalScrollbar(
            adapter = verticalScrollStateAdapter,
            style = ScrollbarStyle(
                minimalHeight = 16.dp,
                thickness = 8.dp,
                shape = RoundedCornerShape(4.dp),
                hoverDurationMillis = 300,
                unhoverColor = Color.White.copy(alpha = 0.20f),
                hoverColor = Color.White.copy(alpha = 0.50f)
            ),
            modifier = Modifier.fillMaxHeight().background(color = Color.Transparent)
        )
    }
}