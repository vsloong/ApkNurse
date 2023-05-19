import com.vsloong.apknurse.utils.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.filechooser.FileSystemView

/**
 * 获取欢迎语时间
 */
fun welcomeTime(): String {
    val date = Date()
    val df = SimpleDateFormat("HH")
    val str = df.format(date)
    val a = str.toInt()
    if (a in 0..6) {
        return "凌晨"
    }
    if (a in 7..12) {
        return "上午"
    }
    if (a in 13..13) {
        return "中午"
    }
    if (a in 14..18) {
        return "下午"
    }
    if (a in 19..24) {
        return "晚上"
    }
    return "不知道"
}


/**
 * 建立工程的工作区目录
 * 工程运行后资源文件，生成的相关文件等都会统一在该目录中处理
 *
 * Win下是
 * C:\Users\你的用户名\Documents\ApkNurse
 *
 * MAC下是
 * /Users/你的用户名/ApkNurse
 */
fun localWorkspaceDirPath(): String {
    val dirName = "ApkNurse"

    // 如果是win系统，那么默认放到d盘下的相关文件夹，如果d盘不存在，那么放入到系统默认目录下
    if (isWindows()) {
        val d = File("d:")
        if (d.exists()) {
            val workspaceDir = File("d:\\${dirName}")
            return workspaceDir.absolutePath
        }
    }

    return FileSystemView.getFileSystemView().defaultDirectory.absolutePath +
        File.separator + dirName
}

/**
 * 本地缓存的资源相关目录
 * localWorkspace/libs
 */
fun localLibsDirPath(): String {
    return localWorkspaceDirPath() +
        File.separator + "libs"
}

/**
 * 本地缓存的临时相关目录
 * localWorkspace/temp
 */
fun localTempDirPath(): String {
    return localWorkspaceDirPath() +
        File.separator + "temp"
}

/**
 * 本地处理工程创建的工程文件夹根目录
 */
fun localProjectsDirPath(): String {
    return localWorkspaceDirPath() +
        File.separator + "projects"
}

/**
 * 获取ApkTool.jar路径
 */
fun localApkToolJarPath(): String {
    return localLibsDirPath() +
        File.separator + LIB_NAME_APK_TOOL
}

/**
 * 获取aapt2文件路径（区分win及mac系统）
 * 基于build-tools 32.0.0版本
 */
fun localAapt2Path(): String {
    return localLibsDirPath() +
        File.separator + LIB_NAME_AAPT2
}

/**
 * 本地JD-GUI的jar包地址
 */
fun localJdGuiJarPath(): String {
    return localLibsDirPath() +
        File.separator + LIB_NAME_JD_GUI
}

/**
 * 本地Procyon的jar包地址
 */
fun localProcyonJarPath(): String {
    return localLibsDirPath() +
        File.separator + LIB_NAME_PROCYON
}