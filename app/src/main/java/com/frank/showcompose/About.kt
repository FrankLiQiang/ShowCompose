package com.frank.showcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun showAbout(modifier: Modifier = Modifier) {
    if (isToDraw > 100) return

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
        Row(Modifier.weight(3.0f)) {}
        Row(Modifier.weight(1.5f)) {
            Row(Modifier.weight(1.0f)) {}
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            switchLanguage(Locale.ENGLISH)
                            isToDraw = 1 - isToDraw
                        },
                    painter = painterResource(R.drawable.en),
                    contentDescription = stringResource(id = R.string.app_name),
                )
                Text(
                    text = "English",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
            }
            Row(Modifier.weight(1.0f)) {}
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            switchLanguage(Locale.CHINA)
                            isToDraw = 1 - isToDraw
                        },
                    painter = painterResource(R.drawable.zh),
                    contentDescription = stringResource(id = R.string.app_name),
                )
                Text(
                    text = "中文",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                )
            }
            Row(Modifier.weight(1.0f)) {}
        }
        Row(Modifier.weight(3.0f)) {}
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

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    showAbout()
}
