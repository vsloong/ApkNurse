package com.vsloong.apknurse.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.vsloong.apknurse.bean.EditorType

/**
 * 编辑器的ViewModel
 * 文本编辑器、图片编辑器
 */
class EditorViewModel {

    /**
     * 编辑器的类型
     */
    val editorType = mutableStateOf<EditorType>(EditorType.NONE)

}