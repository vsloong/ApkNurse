package com.vsloong.apknurse.bean

/**
 * APK基础信息
 * 注意，只存储获取基本的必备信息
 */
data class ApkBasicInfo(
    var appName: String = "",
    var packageName: String = "",
    var versionCode: String = "",
    var versionName: String = "",
)