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
data class FileItemInfo(
    val parent: String,             // 父文件夹路径
    val name: String,               // 真实的文件名
    val showName: String = "",      // 对外展示的文件名（默认空，则展示name,不为空则展示）
    val isDir: Boolean,
    var selected: Boolean = false,
    var depth: Int = 0,
    val isRootFile: Boolean, // 是否是根文件夹
)
