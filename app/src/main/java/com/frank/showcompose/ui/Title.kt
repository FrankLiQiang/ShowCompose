package com.frank.showcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.showcompose.CHOOSE_FILE
import com.frank.showcompose.CHOOSE_IMAGE
import com.frank.showcompose.CHOOSE_LIST
import com.frank.showcompose.CHOOSE_TEXT
import com.frank.showcompose.Information
import com.frank.showcompose.R
import com.frank.showcompose._hideFileName
import com.frank.showcompose._hideFilePath
import com.frank.showcompose.gotoLast
import com.frank.showcompose.infoPercent
import com.frank.showcompose.isShowConfirm
import com.frank.showcompose.isToDraw
import com.frank.showcompose.menuIndex
import com.frank.showcompose.mutableData
import com.frank.showcompose.myTitle
import com.frank.showcompose.saveImage
import com.frank.showcompose.uriHide
import kotlinx.coroutines.launch

@Composable
fun ShowTile(drawerState: DrawerState, modifier: Modifier) {
    if (isToDraw > 100) return
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    ShowPasswordDialog()
    Row(modifier = Modifier.height(45.dp)) {
    }
    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .padding(start = 15.dp)
                .clickable {
                    scope.launch {
                        drawerState.open()
                    }
                },
            painter = painterResource(id = R.drawable.icon_menu),
            contentDescription = stringResource(id = R.string.app_name),
        )
        Row {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = myTitle,
                color = Color.White,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
            )
        }
        Row {
            Row(modifier = Modifier.weight(1.0f)) {}
            if (!isReadOnly && (menuIndex == CHOOSE_LIST || menuIndex == CHOOSE_TEXT || menuIndex == CHOOSE_FILE)) {
                Image(
                    modifier = Modifier
                        .padding(end = 15.dp, bottom = 10.dp)
                        .clickable { saveImage(context) },
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = stringResource(id = R.string.app_name),
                )
            }
            if (menuIndex == CHOOSE_LIST && !isReadOnly) {
                Image(
                    modifier = Modifier
                        .padding(end = 15.dp, bottom = 10.dp)
                        .clickable {
                            mutableData.add(Information())
                            gotoLast()
                            myTitle =
                                context.getString(R.string.list) + "(${mutableData.size})"
                            isToDraw = 1 - isToDraw
                        },
                    painter = painterResource(id = R.drawable.add_item),
                    contentDescription = stringResource(id = R.string.app_name),
                )
            }
        }
    }
    if (isToDraw < 100 &&
        (menuIndex == CHOOSE_LIST ||
                menuIndex == CHOOSE_IMAGE ||
                menuIndex == CHOOSE_TEXT ||
                menuIndex == CHOOSE_FILE)
    ) {
        if (isDoing) {
            LinearProgressIndicator(
                color = colorResource(id = R.color.title),
                trackColor = Color.LightGray,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LinearProgressIndicator(
                color = colorResource(id = R.color.title),
                trackColor = Color.LightGray,
                progress = infoPercent,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    if (isToDraw < 100 &&
        (menuIndex == CHOOSE_LIST ||
                menuIndex == CHOOSE_IMAGE ||
                menuIndex == CHOOSE_TEXT ||
                menuIndex == CHOOSE_FILE)
    ) {
        if (isDoing) {
            LinearProgressIndicator(
                color = colorResource(id = R.color.title),
                trackColor = Color.LightGray,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LinearProgressIndicator(
                color = colorResource(id = R.color.title),
                trackColor = Color.LightGray,
                progress = infoPercent,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun showConfirm(isItemDel: Boolean, event: () -> Unit = {}) {
    val isItemDel = isItemDel
    val context = LocalContext.current
    if (isShowConfirm) {
        AlertDialog(
            title = {
                Text(text = context.getString(R.string.app_name))
            },
            text = {
                Text(text = context.getString(R.string.delete_file))
            },
            onDismissRequest = {
                isShowConfirm = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (isItemDel) {
                            event()
                        } else {
                            _hideFileName = ""
                            _hideFilePath = ""
                            uriHide = null
                        }
                        isShowConfirm = false
                    }
                ) {
                    Text(context.getString(R.string.library_btnOK))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isShowConfirm = false
                    }
                ) {
                    Text(context.getString(R.string.library_btnCancel))
                }
            }
        )
    }
}
