package com.vsloong.apknurse.utils

/**
 * 获取系统名，根据名称判断系统类型
 */
fun getSystem(): String {
    return System.getProperties().getProperty("os.name")
}

/**
 * 判断是否是mac
 */
fun isMac(): Boolean {
    return getSystem().lowercase().contains("mac")
}

/**
 * 判断是否是windows
 */
fun isWindows(): Boolean {
    return getSystem().lowercase().contains("windows")
}

/**
 * 判断是否是linux
 */
fun isLinux(): Boolean {
    val osName = getSystem().lowercase()
    return osName.contains("nix")
        || osName.contains("nux")
        || osName.contains("aix")
}

/**
 * 获取cpu架构
 */
fun geChip(): String {
    return System.getProperties().getProperty("os.arch")
}

/**
 * 判断是否是arm架构
 */
fun isArm(): Boolean {
    return geChip().lowercase().contains("aarch64")
}

/**
 * 判断是否是amd64架构
 */
fun isAmd64(): Boolean {
    return geChip().lowercase().contains("amd64")
}

/**
 * 判断是否是x86_64架构
 */
fun isX86_64(): Boolean {
    return geChip().lowercase().contains("x86_64")
}