package com.frank.showcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun showAbout(modifier: Modifier = Modifier) {
    val content = LocalContext.current
    Column(
        modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(Modifier.weight(1.0f)) {}
        Row(Modifier.height(230.dp)) {
            Row(Modifier.weight(1.0f)) {}
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                modifier = Modifier.fillMaxSize(),
                contentDescription = stringResource(id = R.string.app_name),
            )
            Row(Modifier.weight(1.0f)) {}
        }
        Row(Modifier.weight(10.0f)) {}
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = content.getString(R.string.app_name),
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textAlign = TextAlign.Center,
            text = content.getString(R.string.copy_right),
            color = Color.Black
        )
    }
}