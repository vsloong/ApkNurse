package com.vsloong.apknurse.usecase


import com.vsloong.apknurse.utils.logger
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * 处理XML文件的用例
 */
class XmlUseCase {

    /**
     * 添加网络安全配置到清单文件
     */
    fun addNetWorkSecurityConfigToManifest(
        xmlFilePath: String,
        outputFilePath: String
    ) {
        val reader = SAXReader()
        reader.encoding = "utf-8"

        val inputStream = FileInputStream(xmlFilePath)
        val document = reader.read(inputStream)
        inputStream.close()

        val root = document.rootElement

        // 获取到 application 节点
        val applicationElement = root.element("application")

        // 先检查是否有相应的节点，有则修改，无则添加
        var isContainsNet = false

        run loop@{
            applicationElement.attributes().forEach {
                logger("application属性： ${it.qName.qualifiedName} -> ${it.value}")
                if (it.qName.qualifiedName == "android:networkSecurityConfig") {
                    it.value = "@xml/network_security_config"
                    isContainsNet = true

                    return@loop
                }
            }
        }
        if (!isContainsNet) {
            applicationElement.addAttribute(
                "android:networkSecurityConfig", "@xml/network_security_config"
            )
        }

        writeDocumentToFile(
            document = document,
            outputFilePath = outputFilePath
        )
    }

    /**
     * 创建一个network_security_config.xml文件
     *
     * <?xml version="1.0" encoding="utf-8"?>
     * <network-security-config>
     *     <base-config cleartextTrafficPermitted="true">
     *         <trust-anchors>
     *             <certificates src="system" overridePins="true" />
     *             <certificates src="user" overridePins="true" />
     *         </trust-anchors>
     *     </base-config>
     * </network-security-config>
     */
    fun createNetWorkSecurityConfigXml(
        outputFilePath: String
    ) {

        val document = DocumentHelper.createDocument()

        val networkSecurityConfigElement = document.addElement("network-security-config")
        val baseConfigElement = networkSecurityConfigElement.addElement("base-config")
        baseConfigElement.addAttribute("cleartextTrafficPermitted", "true")

        val trustAnchorsElement = baseConfigElement.addElement("trust-anchors")

        trustAnchorsElement.addElement("certificates")
            .addAttribute("src", "system")
            .addAttribute("overridePins", "true")

        trustAnchorsElement.addElement("certificates")
            .addAttribute("src", "user")
            .addAttribute("overridePins", "true")

        writeDocumentToFile(
            document = document,
            outputFilePath = outputFilePath
        )
    }
}

private fun writeDocumentToFile(
    document: Document,
    outputFilePath: String
) {

    var xmlWriter: XMLWriter? = null
    var outputStream: FileOutputStream? = null
    try {
        val litterFile = File(outputFilePath)
        outputStream = FileOutputStream(litterFile)

        val outputFormat = OutputFormat.createPrettyPrint()
        outputFormat.encoding = "UTF-8"

        xmlWriter = XMLWriter(outputStream, outputFormat)
        xmlWriter.write(document)
    } catch (e: Throwable) {
        e.printStackTrace()
    } finally {
        xmlWriter?.close()
        outputStream?.close()
    }
}