package com.vsloong.apknurse.bean.state

import androidx.compose.runtime.Stable


/**
 * 左侧栏中的相关状态
 */
@Stable
data class LeftBarState(
    var projectOn: Boolean = false
)
