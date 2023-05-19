package com.vsloong.apknurse.bean

/**
 * 编辑器类型的密封类
 */
sealed class EditorType {

    object NONE : EditorType()
    class TEXT(val textContent: String, val textType: String) : EditorType()
    class IMAGE(val imagePath: String) : EditorType()
}
