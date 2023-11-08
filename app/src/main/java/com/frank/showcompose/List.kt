package com.frank.showcompose

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.showcompose.ui.isReadOnly
import com.frank.showcompose.ui.showConfirm
import kotlinx.coroutines.launch
import java.text.Collator
import java.util.Locale
import java.util.regex.Pattern

data class Information(
    var item: String = "",
    var password: String = "",
    var isItemChosen: Boolean = false,
)

var listInfo = ""

var mutableData = mutableListOf(Information())
var isToDraw by mutableStateOf(0)
var isItem by mutableStateOf(true)

fun addData() {
    val sList = listInfo.split("\t")
    mutableData.clear()
    for (i in 0..sList.size - 2 step 2) {
        mutableData.add(Information(item = sList[i], password = sList[i + 1]))
    }
}

fun getListData() {
    fun isChinese(str: String): Boolean {
        val bb = str.substring(0, 1)
        return Pattern.matches("[\u4E00-\u9FA5]", bb)
    }

    val integerChars = '0'..'9'
    fun isInteger(input: String) = input.all { it in integerChars }
    val c1: Comparator<Information> = Comparator { o1, o2 ->
        if (!isChinese(o1.item) && !isChinese(o2.item)) {
            if (isInteger(o1.item.substring(0, 1))) {
                if (isInteger(o2.item.substring(0, 1))) {
                    o1.item.compareTo(o2.item, ignoreCase = true)
                } else {
                    -1
                }
            } else {
                if (isInteger(o2.item.substring(0, 1))) {
                    1
                } else {
                    o1.item.compareTo(o2.item, ignoreCase = true)
                }
            }
        } else if (!isChinese(o1.item)) {
            -1
        } else if (!isChinese(o2.item)) {
            1
        } else {
            val instance = Collator.getInstance(Locale.CHINA)
            instance.compare(o1.item, o2.item)
        }
    }
    mutableData.sortWith(c1)
    listInfo = ""
    for (m in mutableData) {
        listInfo += m.item + "\t" + m.password + "\t"
    }
}

var lastClickItem: Information? = null

lateinit var gotoLast: () -> Unit
var searchString by mutableStateOf("")
var isImeVisible by mutableStateOf(false)
var searchStart by mutableStateOf(0)

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ShowList() {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    gotoLast = {
        coroutineScope.launch {
            scrollState.scrollToItem(mutableData.size - 1)
        }
    }
    val imeVisible = WindowInsets.Companion.isImeVisible
    LaunchedEffect(WindowInsets.isImeVisible) {
        isImeVisible = imeVisible
    }


    Column(modifier = Modifier.background(Color.White)) {
        Row(
            modifier = Modifier
                .height(60.dp)
                .background(colorResource(R.color.title))
        ) {
            TextField(
                modifier = Modifier.weight(1.0f),
                value = searchString,
                textStyle = TextStyle(
                    color = Color.Black,
                    textIndent = TextIndent(12.sp),
                    background = Color.White,
                    fontSize = 19.sp,
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                maxLines = 1,
                onValueChange = { searchString = it },
            )
            Image(
                modifier = Modifier
                    .background(colorResource(id = R.color.title))
                    .padding(15.dp)
                    .clickable
                    {
                        if (searchString.isEmpty()) return@clickable

                        for (m in mutableData) {
                            m.isItemChosen = false
                        }
                        var isFound = false
                        for (i in searchStart until mutableData.size) {
                            val m = mutableData[i]
                            if ((m.item
                                    .uppercase()
                                    .contains(searchString.uppercase()))
                                || (m.password
                                    .uppercase()
                                    .contains(searchString.uppercase()))
                            ) {

                                isFound = true
                                searchStart = i + 1

                                m.isItemChosen = true
                                lastClickItem = m
                                coroutineScope.launch {
                                    scrollState.scrollToItem(i)
                                }
                                break
                            }
                        }
                        if (!isFound) {
                            searchStart = 0
                            Toast
                                .makeText(context, R.string.not_found, Toast.LENGTH_SHORT)
                                .show()
                        }
                        keyboardController?.hide() // 隐藏软键盘
                        isToDraw = 1 - isToDraw
                    },
                painter = painterResource(id = R.drawable.search),
                contentDescription = stringResource(id = R.string.app_name),
            )
        }
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
        ) {
            if (isToDraw < -1) return@LazyColumn

            items(mutableData) { menuItem ->

                if (!isReadOnly && menuItem.isItemChosen && isItem) {
                    TextField(
                        value = menuItem.item,
                        textStyle = TextStyle(
                            color = Color.Black,
                            background = Color.White,
                            fontSize = myFontSize.sp,
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        onValueChange = {
                            menuItem.item = it
                            isToDraw = 1 - isToDraw
                        },
                    )
                } else {
                    NavigationDrawerItem(
                        modifier = Modifier.height(60.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.White,
                            unselectedContainerColor = Color.White,
                        ),
                        shape = MaterialTheme.shapes.small,
                        selected = true,
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_brightness_1_24),
                                contentDescription = stringResource(id = R.string.app_name),
                                tint = if (menuItem.isItemChosen) Color.Red else Color.DarkGray,
                            )
                        },
                        label = {
                            Row {
                                Text(
                                    text = menuItem.item,
                                    fontSize = myFontSize.sp,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        },
                        onClick = {
                            if (lastClickItem != null && menuItem != lastClickItem) {
                                lastClickItem!!.isItemChosen = false
                            }
                            menuItem.isItemChosen = !menuItem.isItemChosen
                            if (menuItem.isItemChosen) {
                                lastClickItem = menuItem
                            }
                            isItem = true
                            isToDraw = 1 - isToDraw
                        },
                    )
                }
                if (menuItem.isItemChosen) {
                    if (!isReadOnly && !isItem) {
                        Row() {
                            showConfirm(true) {
                                mutableData.remove(menuItem)
                                isToDraw = 1 - isToDraw
                            }
                            Button(onClick = { isShowConfirm = true }) {
                                Text(text = stringResource(id = R.string.item_swipe_menu_delete))
                            }
                            TextField(
                                value = menuItem.password,
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    background = Color.White,
                                    fontSize = myFontSize.sp,
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                ),
                                onValueChange = {
                                    menuItem.password = it
                                    isToDraw = 1 - isToDraw
                                },
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                        )
                        {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.DarkGray,
                                                Color.LightGray,
                                            ),
                                            startY = 0f,
                                            endY = 30f,
                                            tileMode = TileMode.Clamp
                                        )
                                    )
                                    .padding(5.dp)
                                    .padding(start = 50.dp)
                                    .clickable {
                                        if (isReadOnly) {
                                            menuItem.isItemChosen = false
                                        }
                                        isItem = false
                                        isToDraw = 1 - isToDraw
                                    },
                                text = menuItem.password,
                                fontSize = myFontSize.sp,
                                color = Color.Black,
                                lineHeight = (myFontSize + 2).sp,
                                maxLines = 10,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
                Divider(color = Color.LightGray)
            }
        }
        Slider(
            value = myFontSize,
            onValueChange = { myFontSize = it },
            valueRange = 0f..40f,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
        if (isImeVisible) {
            Row(modifier = Modifier.height(350.dp)) {}
        }
    }
}
