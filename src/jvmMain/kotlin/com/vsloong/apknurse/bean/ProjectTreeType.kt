package com.vsloong.apknurse.bean

/**
 * 工程树的类型
 */
sealed class ProjectTreeType {
    class PROJECT(val name: String = "Project") : ProjectTreeType()
    class PACKAGES(val name: String = "Packages") : ProjectTreeType()
}