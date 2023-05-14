package com.vsloong.apknurse.utils

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * 运行CMD指令
 */
fun runCMDArray(
    elements: Array<String>,
    directory: File? = null,
    onLine: (String) -> Unit = {}
): Int {
    return runCMD(elements = elements, directory = directory, onLine = onLine)
}

/**
 * 运行CMD指令
 * 区分win和unix（Mac OS和Linux）环境，一般执行exe或者unix上的可执行文件
 *
 * @param winCMD win命令前缀
 * @param unixCMD unix命令前缀
 * @param cmdSuffix 统一的后缀，会拼接到前面的数组上
 */
fun runCMD(
    winCMD: Array<String>,
    unixCMD: Array<String>,
    cmdSuffix: Array<String> = emptyArray(),
    directory: File? = null,
    onLine: (String) -> Unit = {}
): Int {

    val cmd = if (isWindows()) {
        winCMD
    } else {
        unixCMD
    } + cmdSuffix

    return runCMD(elements = cmd, directory = directory, onLine = onLine)
}

/**
 * 运行CMD指令
 * 不区分系统环境，一般执行jar文件
 */
fun runCMD(
    vararg elements: String,
    directory: File? = null,
    onLine: (String) -> Unit = {}
): Int {
    val cmdStringBuilder = StringBuilder()
    elements.forEach {
        cmdStringBuilder.append(it).append(" ")
    }
    logger("执行cmd命令：$cmdStringBuilder")
    val process = ProcessBuilder(*elements)
        .directory(directory)
        .redirectErrorStream(true)
        .start()
    val reader = BufferedReader(
        InputStreamReader(
            process.inputStream,
            Charset.forName("UTF-8")
        )
    )
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        line?.let {
            logger("CMD执行输出: $line")
            onLine(it)
        }
    }
    return process.waitFor()
}