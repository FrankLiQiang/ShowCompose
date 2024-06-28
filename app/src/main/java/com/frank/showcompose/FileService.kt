package com.frank.showcompose

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Objects
import kotlin.system.exitProcess

object FileService {
    fun createFileWithByte(bytes: ByteArray?, fileName: String?) {
        val file = File(fileName!!)
        var outputStream: FileOutputStream? = null
        var bufferedOutputStream: BufferedOutputStream? = null
        try {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            outputStream = FileOutputStream(file)
            bufferedOutputStream = BufferedOutputStream(outputStream)
            bufferedOutputStream.write(bytes)
            bufferedOutputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close()
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
        }
    }

    fun saveFile(context: Context, bmp: Bitmap): Int {
        try {
            val pfd: ParcelFileDescriptor? =
                uriSource?.let { context.contentResolver.openFileDescriptor(it, "w") }
            val fileOutputStream = FileOutputStream(
                Objects.requireNonNull<ParcelFileDescriptor?>(pfd).fileDescriptor
            )
            val isOk = bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
            pfd!!.close()
            return if (isOk) 0 else 1
        } catch (_: Exception) {
        }
        return 1
    }

    @Throws(Exception::class)
    fun writeFileSelf(context: Context, bFile: ByteArray?) {
        val pfd: ParcelFileDescriptor? =
            uriSelf?.let { context.contentResolver.openFileDescriptor(it, "w") }
        val fileOutputStream =
            FileOutputStream(Objects.requireNonNull<ParcelFileDescriptor?>(pfd).fileDescriptor)
        fileOutputStream.write(bFile)
        fileOutputStream.close()
        pfd!!.close()
    }

    @Throws(Exception::class)
    fun readFile(filename: String?): ByteArray? {
        val file = File(filename!!)
        val inStream = FileInputStream(file)
        val buffer = ByteArray(1024)
        var len: Int
        val outStream = ByteArrayOutputStream()
        while (inStream.read(buffer).also { len = it } != -1) {
            outStream.write(buffer, 0, len)
        }
        val data = outStream.toByteArray()
        outStream.close()
        inStream.close()
        return data
    }

    @Throws(Exception::class)
    fun readFileSelf(context: Context, uri: Uri?): ByteArray {
        val pfd: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(uri!!, "r")
        val inStream =
            FileInputStream(Objects.requireNonNull<ParcelFileDescriptor?>(pfd).getFileDescriptor())
        val buffer = ByteArray(1024)
        var len: Int
        val outStream = ByteArrayOutputStream()
        while (inStream.read(buffer).also { len = it } != -1) {
            outStream.write(buffer, 0, len)
        }
        val data = outStream.toByteArray()
        outStream.close()
        inStream.close()
        return data
    }
}

fun deleteFileAndExit() {
    try {
        if (uriHide != null) {
            val file = File(Objects.requireNonNull(uriHide!!.path))
            if (file.exists()) {
                file.delete()
            }
        }
        exitProcess(0)
    } catch (e: Exception) {
        e.toString()
    }
}

