package com.frank.showcompose

import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.frank.showcompose.InfoThread.Companion.convertByteOrder
import com.frank.showcompose.ui.isDoing

class SelfFileThread(private val _context: Context) : Thread() {
    override fun run() {
        isDoing = true
        try {
            val bFile = FileService.readFileSelf(_context, uriSelf)
            val passwordBytes = _selfFileName.toByteArray(charset("UNICODE"))
            convertByteOrder(passwordBytes)
            val passwordLength = passwordBytes.size
            val length = bFile.size
            for (i in 0 until length) {
                bFile[i] = (bFile[i].toInt() xor passwordBytes[i % passwordLength].toInt()).toByte()
            }
            FileService.writeFileSelf(_context, bFile)
            Looper.prepare();
            Toast.makeText(_context, R.string.messageSaveOk, Toast.LENGTH_SHORT).show()
            Looper.loop();
        } catch (e: Exception) {
            Looper.prepare();
            Toast.makeText(_context, R.string.messageSaveFailed, Toast.LENGTH_SHORT).show()
            Looper.loop();
        }
        isDoing = false
    }
}