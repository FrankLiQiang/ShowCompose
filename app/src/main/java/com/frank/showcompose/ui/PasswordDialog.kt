package com.frank.showcompose.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.frank.showcompose.InfoThread
import com.frank.showcompose.R

var isShowDialog by mutableStateOf(false)
var isConfirm by mutableStateOf(false)
var isReadOnly by mutableStateOf(true)
var password1 by mutableStateOf("")
var password2 by mutableStateOf("")
var isDoing by mutableStateOf(false)

@Composable
private fun CustomDialogWithResultExample(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (String, String) -> Unit,
) {
    var p1 by remember { mutableStateOf(password1) }
    var p2 by remember { mutableStateOf(password1) }
    var hasFocus by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (!hasFocus) focusRequester.requestFocus()
    }
    Dialog(onDismissRequest = onDismiss) {

        Card(
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(modifier = Modifier.padding(8.dp)) {

                Text(
                    text = stringResource(id = R.string.password),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Color Selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.ReadOnly),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1.0f)
                    )

                    Checkbox(
                        checked = isReadOnly,
                        onCheckedChange = { isReadOnly = it },
                        enabled = true,
                        //colors = CheckboxDefaults.colors(custom),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(3.dp)
                    )
                    Row(modifier = Modifier.weight(1.0f)) {}
                    Spacer(modifier = Modifier.height(8.dp))
                }

                TextField(
                    value = p1,
                    onValueChange = { p1 = it },
                    //label = { Text("Enter password") },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { hasFocus = it.isFocused },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                if (isConfirm) {
                    TextField(
                        value = p2,
                        onValueChange = { p2 = it },
                        //label = { Text("Enter password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                ) {

                    TextButton(onClick = onNegativeClick) {
                        Text(text = stringResource(id = R.string.library_btnCancel))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {
                        onPositiveClick(p1, p2)
                    }) {
                        Text(text = stringResource(id = R.string.library_btnOK))
                    }
                }
            }
        }
    }
}

@Composable
fun ShowPasswordDialog() {
    if (isShowDialog) {
        val context = LocalContext.current
        fun checkInputPassword(p1: String, p2: String): Boolean {
            if (p1.isEmpty()) {
                Toast.makeText(context, R.string.messageMustInputPassword, Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (isConfirm) {
                if (p1 != p2) {
                    Toast.makeText(context, R.string.messageSavePassword, Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            return true
        }

        CustomDialogWithResultExample(
            onDismiss = {
                isShowDialog = false
            },
            onNegativeClick = {
                isShowDialog = false
            },
            onPositiveClick = { p1, p2 ->
                if (!checkInputPassword(p1, p2)) {
                    return@CustomDialogWithResultExample
                }
                isShowDialog = false
                password1 = p1
                password2 = p2
                InfoThread(context).start()
            },
        )
    }
}
