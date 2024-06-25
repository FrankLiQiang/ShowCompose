package com.frank.showcompose

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.showcompose.ui.isConfirm
import com.frank.showcompose.ui.isReadOnly
import com.frank.showcompose.ui.isShowDialog
import com.frank.showcompose.ui.showConfirm

var isShowConfirm by mutableStateOf(false)

fun saveImage(context: Context) {
    getListData()
    if (originalBMP == null) {
        Toast.makeText(context, R.string.infoBeforeChoose, Toast.LENGTH_SHORT).show()
    } else if (isReadOnly) {
        Toast.makeText(context, R.string.ReadOnly, Toast.LENGTH_SHORT).show()
    } else if (textInfo.isEmpty() && listInfo.isEmpty() && uriHide == null) {
        Toast.makeText(context, R.string.infoEmpty, Toast.LENGTH_SHORT).show()
    } else {
        isConfirm = true
        isShowDialog = true
    }
}

@Composable
fun BottomBar() {
    val context = LocalContext.current
    Row(
        Modifier
            .height(130.dp)
            .background(Color.Transparent)
    ) {
        TabItem(
            R.drawable.open_img,
            context.getString(R.string.choose_img),
            Color.LightGray,
            openBMP,
            Modifier
                .weight(1f)
        )
        TabItem(
            R.drawable.key,
            context.getString(R.string.decode),
            Color.LightGray,
            {
                if (originalBMP == null) {
                    Toast.makeText(context, R.string.infoBeforeChoose, Toast.LENGTH_SHORT).show()
                } else {
                    isConfirm = false
                    isReadOnly = true
                    isShowDialog = true
                }
            },
            Modifier.weight(1f)
        )
        TabItem(
            R.drawable.lock,
            context.getString(R.string.encode),
            Color.LightGray,
            { saveImage(context) },
            Modifier.weight(1f)
        )
    }
}

@Composable
fun FileBottomBar() {
    val context = LocalContext.current
    Row(
        Modifier
            .height(130.dp)
            .background(Color.Transparent)
    ) {
        TabItem(
            R.drawable.open_img,
            context.getString(R.string.choose_file),
            Color.DarkGray,
            openHide,
            Modifier.weight(1f)
        )
        TabItem(
            R.drawable.clear,
            context.getString(R.string.del_chosen),
            Color.DarkGray,
            {
                if (isReadOnly) {
                    Toast.makeText(context, R.string.ReadOnly, Toast.LENGTH_SHORT).show()
                } else {
                    isShowConfirm = true
                }
            },
            Modifier.weight(1f)
        )
        showConfirm(false)
    }
}

@Composable
fun FileSelfBottomBar() {
    val context = LocalContext.current
    Row(
        Modifier
            .height(130.dp)
            .background(Color.Transparent)
    ) {
        TabItem(
            R.drawable.open_img,
            context.getString(R.string.choose_file),
            Color.DarkGray,
            openSelf,
            Modifier.weight(1f)
        )
        TabItem(
            R.drawable.refresh,
            context.getString(R.string.decode),
            Color.DarkGray,
            {
                SelfFileThread(context).start()
            },
            Modifier.weight(1f)
        )
    }
}

@Composable
fun TabItem(
    @DrawableRes iconId: Int,
    title: String,
    tint: Color,
    event: () -> Unit,
    modifier: Modifier = Modifier
) {
    val count = remember { mutableStateOf(0) }
    Column(
        modifier
            .padding(vertical = 8.dp)
            .background(if (count.value == 1) Color.Cyan else Color.Transparent)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { count.value = 1 }, // 按下
//                    onDoubleTap = { count.value = 2 }, // 双击
//                    onLongPress = { count.value = 3 }, // 长按
                    onTap = {
                        count.value = 4
                        event()
                    } // 单击
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(iconId), title,
            Modifier
                .size(50.dp)
                .fillMaxWidth(),
            tint = tint
        )
        Text(title, fontSize = 15.sp, color = tint)
    }
}

