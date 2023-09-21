package com.frank.showcompose

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.frank.showcompose.ui.MyNavDrawerApp
import com.frank.showcompose.ui.isReadOnly
import com.frank.showcompose.ui.theme.ShowComposeTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

const val CHOOSE_IMAGE = 0
const val CHOOSE_LIST = 1
const val CHOOSE_TEXT = 2
const val CHOOSE_FILE = 3
const val CHOOSE_FILE_SELF = 4
const val CHOOSE_FILE_IMAGE = 5
const val CHOOSE_HELP = 6
const val CHOOSE_ABOUT = 7
const val CHOOSE_FILE_HIDE = 8
const val CHOOSE_EXIT = 10

var menuIndex by mutableStateOf(CHOOSE_IMAGE)
var isDrawId by mutableStateOf(0)
var myTitle by mutableStateOf("选择图片")
var originalBMP: Bitmap? = null
lateinit var openBMP: () -> Unit
lateinit var openHide: () -> Unit
lateinit var openSelf: () -> Unit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        mutableData.clear()
        setOpenFileFun()
        setContent {
            ShowComposeTheme {
                TransparentSystemBars()
                MyNavDrawerApp()
            }
        }
    }

    private fun setOpenFileFun() {
        openBMP = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, CHOOSE_FILE_IMAGE)
        }
        openHide = {
            if (isReadOnly) {
                Toast.makeText(this, R.string.ReadOnly, Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "*/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, CHOOSE_FILE_HIDE)
            }
        }
        openSelf = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, CHOOSE_FILE_SELF)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        val uri = data.data

        val start: Int = uri!!.path!!.lastIndexOf("/")
        when (requestCode) {
            CHOOSE_FILE_IMAGE -> {
                originalBMP = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                scaleX0 = 1.0f
                scaleY0 = 1.0f
                translationX0 = 0.0f
                translationY0 = 0.0f
                imagePath = uri.path!!
                uriSource = uri
                isDrawId = 1 - isDrawId
            }

            CHOOSE_FILE_HIDE -> {
                try {
                    _hideFileName = uri.path!!.substring(start + 1)
                    uriHide = uri
                } catch (_: Exception) {
                }
            }

            CHOOSE_FILE_SELF -> {
                try {
                    uriSelf = uri
                    _selfFileName = uriSelf!!.path!!.substring(start + 1)
                    _selfFilePath = uriSelf!!.path!!
                } catch (e: java.lang.Exception) {
                    e.toString()
                }
            }
        }
    }
}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
            isNavigationBarContrastEnforced = false,
        )
    }
}
