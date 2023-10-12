package com.frank.showcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


data class MenuItem(val title: Int, val icon: Int, val index: Int)

var menu = listOf(
    MenuItem(
        title = R.string.choose_img,
        icon = R.drawable.menu_choose,
        CHOOSE_IMAGE
    ),
    MenuItem(
        title = R.string.list,
        icon = R.drawable.menu_list,
        CHOOSE_LIST
    ),
    MenuItem(
        title = R.string.text,
        icon = R.drawable.menu_text,
        CHOOSE_TEXT
    ),
    MenuItem(
        title = R.string.file,
        icon = R.drawable.menu_file,
        CHOOSE_FILE
    ),
    MenuItem(
        title = R.string.file_self,
        icon = R.drawable.menu_file_self,
        CHOOSE_FILE_SELF
    ),
    MenuItem(0, 0, -1),
    MenuItem(
        title = R.string.help,
        icon = R.drawable.menu_help,
        CHOOSE_HELP
    ),
    MenuItem(
        title = R.string.about,
        icon = R.drawable.menu_about,
        CHOOSE_ABOUT
    ),
    MenuItem(0, 0, -1),
    MenuItem(0, 0, -1),
    MenuItem(
        title = R.string.exit,
        icon = R.drawable.menu_exit,
        CHOOSE_EXIT
    ),
)

@Composable
fun showMenuFun(modifier: Modifier) {
    when (menuIndex) {
        -1 -> {}
        CHOOSE_IMAGE -> {
            chooseImg(modifier)
        }

        CHOOSE_LIST -> {
            ShowList()
        }

        CHOOSE_TEXT -> {
            showText(modifier)
        }

        CHOOSE_FILE -> {
            showFile(modifier)
        }

        CHOOSE_FILE_SELF -> {
            showFileSelf(modifier)
        }

        CHOOSE_HELP -> {
            showHelp(modifier)
        }

        CHOOSE_ABOUT -> {
            showAbout(modifier)
        }

        CHOOSE_EXIT -> {
            deleteFileAndExit()
        }
    }
}
