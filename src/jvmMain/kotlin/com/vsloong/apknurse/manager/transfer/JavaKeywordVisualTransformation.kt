package com.vsloong.apknurse.manager.transfer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie
import com.vsloong.apknurse.keywordList
import java.util.*

/**
 * java关键字高亮显示
 */
object JavaKeywordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {


        val keywordColor = Color(0xFFCF8E6D)


        val treeMap = TreeMap<String, String>()
        keywordList.forEach {
            treeMap[it] = it
        }

        // 构建 Aho-Corasick 自动机
        val acdat = AhoCorasickDoubleArrayTrie<String>()
        acdat.build(treeMap)

        val annotatedString = buildAnnotatedString {
            append(text.text)

            val hitList = acdat.parseText(text.text)
            hitList.forEach { hit: AhoCorasickDoubleArrayTrie.Hit<String> ->
                val start = hit.begin
                val end = hit.end + 1
                val pattern = hit.value
                val color = keywordColor

                addStyle(
                    style = SpanStyle(color = keywordColor),
                    start = start,
                    end = end
                )

            }
        }

        return TransformedText(annotatedString, OffsetMapping.Identity)
    }
}