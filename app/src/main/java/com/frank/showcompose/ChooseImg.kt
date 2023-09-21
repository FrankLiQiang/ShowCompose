package com.frank.showcompose

import android.graphics.PointF
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

var start = PointF()
var scaleX0 by mutableStateOf(1.0f)
var scaleY0 by mutableStateOf(1.0f)
var translationX0 by mutableStateOf(0.0f)
var translationY0 by mutableStateOf(0.0f)
var _oldDist = 0f
var imagePath by mutableStateOf("")

private const val _NONE = 0
private const val _DRAG = 1
private const val _ZOOM = 2
var _mode = _NONE
var infoPercent by mutableStateOf(0f)

@Composable
fun chooseImg(modifier: Modifier = Modifier) {
    Box(modifier.background(Color.White)) {
        if (isDrawId > -1 && originalBMP != null) {
            var imgModifier = Modifier.fillMaxSize()
            //imgModifier = addImageOpration(imgModifier)
            Image(
                modifier = imgModifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = scaleX0
                        scaleY = scaleY0
                        translationX = translationX0
                        translationY = translationY0
                    },
                bitmap = originalBMP!!.asImageBitmap(),
                contentDescription = stringResource(id = R.string.app_name)
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = imagePath,
            color = Color.DarkGray
        )
        Column(modifier.fillMaxSize()) {
            Row(Modifier.weight(1.0f)) {}
            BottomBar()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun addImageOpration(modifier: Modifier): Modifier {
    fun spacing(event: MotionEvent): Float {
        if (event.pointerCount < 2) {
            return 0f
        }
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return x * x + y * y
    }

    fun distance(p1: PointF, p2: PointF): Double {
        val x = p1.x - p2.x
        val y = p1.y - p2.y
        return (x * x + y * y).toDouble()
    }

    var m = modifier.pointerInteropFilter {
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {
                _mode = _DRAG
                start.x = it.x
                start.y = it.y
                _oldDist = spacing(it)
                if (_oldDist > 10f) {
//val x: Float = event.getX(0) + event.getX(1)
//val y: Float = event.getY(0) + event.getY(1)
//_midPoint.set(x / 2, y / 2)
                    _mode = _ZOOM
                }
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                _oldDist = spacing(it)
                if (_oldDist > 10f) {
//val x: Float = event.getX(0) + event.getX(1)
//val y: Float = event.getY(0) + event.getY(1)
//_midPoint.set(x / 2, y / 2)
                    _mode = _ZOOM
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (_mode == _DRAG) {
                    translationX0 = it.x - start.x
                    translationY0 = it.y - start.y
                } else if (_mode == _ZOOM) {
                    val newDist = spacing(it).toDouble()
                    if (newDist > 10f) {
                        scaleX0 = (newDist / _oldDist).toFloat()
                        scaleY0 = scaleX0
                    }
                }
            }
        }
        true
    }
    return m
}
