package com.frank.showcompose.ui

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.showcompose.CHOOSE_EXIT
import com.frank.showcompose.CHOOSE_IMAGE
import com.frank.showcompose.CHOOSE_LIST
import com.frank.showcompose.R
import com.frank.showcompose.deleteFileAndExit
import com.frank.showcompose.isToDraw
import com.frank.showcompose.menu
import com.frank.showcompose.menuIndex
import com.frank.showcompose.mutableData
import com.frank.showcompose.myTitle
import com.frank.showcompose.showMenuFun
import kotlinx.coroutines.launch

var exitTime = 0L

@Composable
fun MyNavDrawerApp() {
    if (isToDraw > 100) return

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    ModalNavigationDrawer(
        modifier = Modifier.background(colorResource(id = R.color.title)),
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen || drawerState.isClosed,
        drawerContent = {
            MyDrawerContent(
                onItemSelected = {
                    scope.launch {
                        drawerState.close()
                    }
                    snackbarHostState.currentSnackbarData?.dismiss()
                },
                onBackPress = {
                    if (drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                        return@MyDrawerContent
                    }
                    if (menuIndex != CHOOSE_IMAGE) {
                        menuIndex = CHOOSE_IMAGE
                        return@MyDrawerContent
                    }
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        Toast.makeText(
                            context,
                            R.string.once_more,
                            Toast.LENGTH_SHORT
                        ).show()
                        exitTime = System.currentTimeMillis()
                        return@MyDrawerContent
                    }
                    deleteFileAndExit()
                },
            )
        },
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.title)),
        ) {
            if (isToDraw > 100) return@Column

            ShowTile(
                drawerState,
                Modifier
                    .height(40.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                Modifier
                    .weight(1.0f)
                    .background(Color.White)
            ) {
                myTitle = if (menuIndex == CHOOSE_LIST) {
                    stringResource(id = menu[menuIndex].title) + "(${mutableData.size})"
                } else {
                    stringResource(id = menu[menuIndex].title)
                }
                showMenuFun(Modifier.weight(1.0f))
            }
        }
        //}
    }
}

@Composable
fun MyDrawerContent(
    modifier: Modifier = Modifier,
    onItemSelected: (title: Int) -> Unit,
    onBackPress: () -> Unit,
) {
    ModalDrawerSheet(
        modifier.padding(top = 55.dp),      //红米 Note pro 55.dp     Google 6A   33.dp
        drawerContainerColor = Color.Transparent,
    ) {
        Column(
            modifier
                .fillMaxHeight()
                .requiredWidth(220.dp)
                .background(color = Color.Black.copy(alpha = 0.5f))
        ) {
            LazyColumn {
                items(menu) { menuItem ->
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = colorResource(id = R.color.title),
                            unselectedContainerColor = Color.Transparent,
                        ),
                        shape = MaterialTheme.shapes.small,
                        label = {
                            Row {
                                Text(
                                    text = if (menuItem.index == -1) "" else stringResource(id = menuItem.title),
                                    fontSize = 20.sp,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                if (menuItem.index != -1 && menuItem.index != CHOOSE_EXIT) {
                                    Text(
                                        modifier = modifier.fillMaxWidth(),
                                        text = ">",
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.End,
                                        style = MaterialTheme.typography.labelMedium,
                                    )
                                }
                            }
                        },
                        selected = menuItem.index == menuIndex,
                        icon = {
                            if (menuItem.index != -1) {
                                Icon(
                                    painter = painterResource(menuItem.icon),
                                    contentDescription = stringResource(id = menuItem.title),
                                )
                            }
                        },
                        onClick = {
                            if (menuItem.index != -1) {
                                onItemSelected.invoke(menuItem.title)
                                menuIndex = menuItem.index
                            }
                        },
                    )
                    Divider()
                }
            }
        }
    }
    BackPressHandler {
        onBackPress()
    }
}

@Composable
fun BackPressHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    SideEffect {
        backCallback.isEnabled = enabled
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

