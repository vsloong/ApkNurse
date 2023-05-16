package com.vsloong.apknurse.ui.panel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.apknurse.bean.FolderItemInfo
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.ui.scroll.ScrollPanel
import com.vsloong.apknurse.ui.theme.appBarColor
import com.vsloong.apknurse.ui.theme.textColor

/**
 * Project 面板相关UI
 */

@Composable
fun ProjectPanel(
    modifier: Modifier = Modifier,
    folderList: SnapshotStateList<FolderItemInfo> = NurseManager.showFolderList
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .height(40.dp)
                .background(color = appBarColor)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Project",
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }


        val horizontalScrollState = rememberScrollState()
        val verticalScrollState = rememberLazyListState()


        // 工程树结构
        ScrollPanel(
            modifier = Modifier.weight(1f)
                .background(color = appBarColor)
                .padding(2.dp),
            horizontalScrollStateAdapter = rememberScrollbarAdapter(horizontalScrollState),
            verticalScrollStateAdapter = rememberScrollbarAdapter(verticalScrollState)
        ) {
            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(4.dp), // 设置后会导致VerticalScrollbar的显示异常
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScrollState),
                state = verticalScrollState,
            ) {
                itemsIndexed(
                    items = folderList,
                    key = { index, item ->
                        item.parent + item.name
                    }
                ) { index, item ->
                    ProjectItem(
                        item = item,
                        index = index,
                        onClick = {
                            NurseManager.clickFolderItem(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectItem(
    item: FolderItemInfo,
    index: Int,
    onClick: (FolderItemInfo) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .height(24.dp)
            .widthIn(min = 300.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick(item) }
    ) {

        Spacer(modifier = Modifier.width((item.depth * 16).dp))

        Image(
            painter = painterResource(
                resourcePath = if (item.name.endsWith(".java")) {
                    "icons/file_type_java.svg"
                } else {
                    "icons/bar_left_folder.svg"
                }
            ),
            contentDescription = "",
            modifier = Modifier.size(14.dp)
        )

        Text(
            text = item.name,
            color = textColor,
            fontSize = if (index == 0) {
                18.sp
            } else {
                14.sp
            },
            fontWeight = if (index == 0) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}