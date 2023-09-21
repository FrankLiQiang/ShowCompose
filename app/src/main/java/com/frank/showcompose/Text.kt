package com.frank.showcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.showcompose.ui.isReadOnly

var textInfo by mutableStateOf("")
var myFontSize by mutableStateOf(23f)

@Composable
fun showText(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TextField(
            value = textInfo,
            readOnly = isReadOnly,
            onValueChange = {
                if (!isReadOnly) {
                    textInfo = it
                }
            },
            textStyle = TextStyle(
//                color = Color.Black,
//                background = Color.White,
                fontSize = myFontSize.sp,
            ),
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .background(Color.White),
            colors = TextFieldDefaults.colors(
//                selectionColors = TextSelectionColors(
//                    handleColor = Color.White,
//                    backgroundColor = Color.Blue,
//                ),
//                focusedContainerColor = Color.White,
//                unfocusedContainerColor = Color.White,
//                disabledContainerColor = Color.White,
//                errorContainerColor = Color.White,
            ),
        )
        Slider(
            value = myFontSize,
            onValueChange = { myFontSize = it },
            valueRange = 0f..40f,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
    }
}