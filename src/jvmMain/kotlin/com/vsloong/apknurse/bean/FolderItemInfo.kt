package com.vsloong.apknurse.bean

import androidx.compose.runtime.Stable

/**
 * 项目的文件夹树形结构
 *
 * @param selected 是否选中
 *      如果是文件夹被选中，那么需要打开下一级
 *      如果是文件被选中，那么需要打开文件
 *
 * @param depth 当前文件夹的深度
 */
@Stable
data class FolderItemInfo(
    val parent: String, // 父文件夹路径
    val name: String,
    val isDir: Boolean,
    var selected: Boolean = false,
    var depth: Int = 0
)
