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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.vsloong.apknurse.manager.NurseManager
import com.vsloong.apknurse.ui.scroll.ScrollPanel
import com.vsloong.apknurse.ui.theme.codeTextColor


@Composable
fun EditPanel(
    modifier: Modifier,
    editContent: String = NurseManager.codeEditContent.value
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
                backgroundColor = Color.Transparent
            ),
            textStyle = TextStyle(
                color = codeTextColor,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp
            ),
            modifier = Modifier.fillMaxSize()
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState),
            onValueChange = {},
        )
    }
}