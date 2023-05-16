package com.vsloong.apknurse.ui.panel

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.vsloong.apknurse.ui.scroll.ScrollPanel
import com.vsloong.apknurse.ui.theme.codeTextColor


@Composable
fun EditPanel(
    modifier: Modifier,
    editContent: String
) {

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    ScrollPanel(
        modifier = modifier,
        verticalScrollStateAdapter = rememberScrollbarAdapter(verticalScrollState),
        horizontalScrollStateAdapter = rememberScrollbarAdapter(horizontalScrollState)
    ) {

        TextField(
            value = editContent,
            colors = TextFieldDefaults.textFieldColors(
                textColor = codeTextColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                errorCursorColor = Color.Red,
                backgroundColor = Color.Transparent,

                ),
            textStyle = TextStyle(
                color = codeTextColor,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
                fontFamily = FontFamily.Monospace
            ),
            modifier = Modifier.fillMaxSize()
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState),
            onValueChange = {},
//            visualTransformation = ColorizeCodeVisualTransformation()
        )

    }
}


class ColorizeCodeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val builder = AnnotatedString.Builder()
        val inputText = text.text

        var startIndex = 0
        var endIndex = 0

        while (endIndex < inputText.length) {
            endIndex = inputText.indexOfAny(charArrayOf(' ', ','), startIndex)

            if (endIndex == -1) {
                endIndex = inputText.length
            }

            val word = inputText.substring(startIndex, endIndex)

            val color = when (word) {
                "hello" -> Color.Red
                "final" -> Color.Blue
                else -> null
            }

            if (color != null) {
                builder.withStyle(style = SpanStyle(color = color)) {
                    append(inputText.substring(startIndex, endIndex))
                }
            }


            startIndex = endIndex
            endIndex++
        }

        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }
}