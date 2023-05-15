package com.vsloong.apknurse.manager

import androidx.compose.runtime.mutableStateOf
import com.vsloong.apknurse.bean.ApkBasicInfo
import com.vsloong.apknurse.bean.ApkNurseInfo

object NurseManager {

    val apkNurseInfo = mutableStateOf(ApkNurseInfo())

    fun updateApkNurseInfo(apkBasicInfo: ApkBasicInfo) {
        apkNurseInfo.value = ApkNurseInfo(apkBasicInfo)
    }

    fun getApkNurseInfo(): ApkNurseInfo {
        return apkNurseInfo.value
    }
}