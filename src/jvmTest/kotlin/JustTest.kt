import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.utils.decompressByZip
import java.io.File

fun main() {

//    val currentFile = File("D:\\Works_Compose_Desktop\\ApkNurse\\src\\jvmMain\\resources\\libs\\dex2jar-2.1.zip")
//
//    println("当前文件地址：${currentFile.absolutePath}")
//
//    decompressByZip(
//        zipFilePath = currentFile.absolutePath,
//        outputDirPath = "E:\\ApkTook\\zip"
//    )

    NurseManager.createProjectTree()
}