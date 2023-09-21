package com.frank.showcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.frank.showcompose.ui.isReadOnly

var _selfFileName by mutableStateOf("")
var _selfFilePath by mutableStateOf("")

@Composable
fun showFileSelf(modifier: Modifier = Modifier) {
    var context = LocalContext.current
    Column(
        modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(20.dp)
    ) {
        Text(
            text = context.getString(R.string.en_decode_file),
            color = Color.Black
        )
        TextField(
            value = _selfFilePath,
            readOnly = true,
            onValueChange = {
                if (!isReadOnly) {
                    textInfo = it
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .background(Color.White),
        )
        FileSelfBottomBar()
    }
}