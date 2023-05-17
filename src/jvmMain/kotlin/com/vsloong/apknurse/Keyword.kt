package com.vsloong.apknurse

import androidx.compose.ui.graphics.Color


// 构建模式与颜色的映射关系
private val patternColorMap = mapOf(
    "private" to Color(0xFFCF8E6D),
)

/**
 * java 中的关键字
 * 48 + 3 + 2
 */
val keywordList = listOf(
    "abstract",
    "assert",
    "boolean",
    "break",
    "byte",
    "case",
    "catch",
    "char",
    "class",
    "continue",
    "default",
    "do",
    "double",
    "else",
    "enum",
    "extends",
    "final",
    "finally",
    "float",
    "for",
    "if",
    "implements",
    "import",
    "int",
    "interface",
    "instanceof",
    "long",
    "native",
    "new",
    "package",
    "private",
    "protected",
    "public",
    "return",
    "short",
    "static",
    "strictfp",
    "super",
    "switch",
    "synchronized",
    "this",
    "throw",
    "throws",
    "transient",
    "try",
    "void",
    "volatile",
    "while",

    "true",
    "false",
    "null",

    "goto",
    "const",
)