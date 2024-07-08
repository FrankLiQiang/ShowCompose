package com.frank.showcompose

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Looper
import android.widget.Toast
import com.frank.showcompose.ui.isConfirm
import com.frank.showcompose.ui.isDoing
import com.frank.showcompose.ui.password1
import java.io.File
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.Objects

var uriSource: Uri? = null
var uriSelf: Uri? = null
var uriHide: Uri? = null
lateinit var passwordBytes: ByteArray

class InfoThread(var context: Context) : Thread() {
    private lateinit var bList: ByteArray
    private lateinit var bText: ByteArray
    private var bFile: ByteArray? = null
    private lateinit var bmpByteArray: ByteArray
    private val infoLength = IntArray(7)

    init {
        try {
            bmp2byte()
            passwordBytes = password1.toByteArray(charset("UNICODE"))
            convertByteOrder(passwordBytes)
        } catch (_: Exception) {
        }
    }

    private external fun readProfile(
        `in`: ByteArray?,
        p: ByteArray?,
        listInfoLength: IntArray?
    ): ByteArray?

    private external fun readInfo(
        infoLength: IntArray?,
        headerBytes: ByteArray?,
        bmpBytes: ByteArray?,
        pListInfo: ByteArray?,
        pTextInfo: ByteArray?,
        pFileInfo: ByteArray?,
        pFileName: ByteArray?,
    ): Int

    private external fun saveInfo(
        version: Byte,
        bmpBytes: ByteArray?,
        pFileName: ByteArray?,
        pPassword: ByteArray?,
        pList: ByteArray?,
        pText: ByteArray?,
        pFile: ByteArray?,
        pHashBytes: ByteArray?,
    ): Int

    private fun bmp2byte() {
        val bytes = originalBMP!!.byteCount
        val buf = ByteBuffer.allocate(bytes)
        originalBMP!!.copyPixelsToBuffer(buf)
        bmpByteArray = buf.array()
    }

    private fun byte2bmp() {
        originalBMP =
            Bitmap.createBitmap(originalBMP!!.width, originalBMP!!.height, Bitmap.Config.ARGB_8888)
        originalBMP!!.copyPixelsFromBuffer(ByteBuffer.wrap(bmpByteArray))
    }

    override fun run() {
        isDoing = true
        val result: Int
        if (isConfirm) {
//            val currentTimeMillis = System.currentTimeMillis()
            getListData()
            result = saveAllInfo()
            if (result == 0) {
                byte2bmp()
                val ret = FileService.saveFile(context, originalBMP!!)
                Looper.prepare()
                if (ret == 0) {
                    Toast.makeText(context, R.string.messageSaveOk, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.messageSaveFailed, Toast.LENGTH_SHORT).show()
                }
                isDoing = false
                isToDraw = 1 - isToDraw
//                val currentTimeMillis1 = System.currentTimeMillis()
//                Log.i("AAABBB", "Time = " + (currentTimeMillis1 - currentTimeMillis))
                Looper.loop()
            } else if (result == -2) {
                Looper.prepare()
                Toast.makeText(context, R.string.tooLarge, Toast.LENGTH_SHORT).show()
                isDoing = false
                isToDraw = 1 - isToDraw
                Looper.loop()
            } else {
                Looper.prepare()
                Toast.makeText(context, R.string.messageSaveFailed, Toast.LENGTH_SHORT).show()
                isDoing = false
                isToDraw = 1 - isToDraw
                Looper.loop()
            }
        } else {
            if (readInfoJava() == 0) {
                isDoing = false
                isToDraw = 1 - isToDraw
            } else {
                Looper.prepare()
                Toast.makeText(context, R.string.messagePasswordError, Toast.LENGTH_SHORT).show()
                isDoing = false
                isToDraw = 1 - isToDraw
                Looper.loop()
            }
        }
    }

    private fun readInfoJava(): Int {
        val headerBytes = readProfile(
            bmpByteArray,
            passwordBytes,
            infoLength
        ) ?: return -1

        val listBytes = ByteArray(infoLength[0])
        val textBytes = ByteArray(infoLength[1])
        val fileBytes = ByteArray(infoLength[2])
        val fileNameBytes = ByteArray(infoLength[4])
        readInfo(
            infoLength,
            headerBytes,
            bmpByteArray,
            listBytes,
            textBytes,
            fileBytes,
            fileNameBytes
        )
        try {
            uriHide = null
            _hideFileName = ""
            if (infoLength[2] > 0) {
                _hideFileName = String(fileNameBytes, Charsets.UTF_16)
                _hideFilePath =
                    Objects.requireNonNull<File?>(context.getExternalFilesDir(null)).absolutePath
//                _hideFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()

                uriHide = Uri.parse("$_hideFilePath/$_hideFileName")
                FileService.createFileWithByte(fileBytes, uriHide!!.path)
                menuIndex = CHOOSE_FILE
            }

            if (infoLength[1] > 0) {
                textInfo = String(textBytes, Charsets.UTF_16)
                menuIndex = CHOOSE_TEXT
            }

            if (infoLength[0] > 0) {
                listInfo = String(listBytes, Charsets.UTF_16)
                addData()
                menuIndex = CHOOSE_LIST
            }
            setPercent(infoLength[0] + infoLength[1] + infoLength[2])
        } catch (_: Exception) {
            return -1
        }
        return 0
    }

    private fun concatArrays(vararg arrays: ByteArray): ByteArray {
        var result = byteArrayOf()
        arrays.forEach { array ->
            result += array
        }
        return result
    }

    private fun saveAllInfo(): Int {
        try {
            var fileNameBytes = ByteArray(0)
            bList = listInfo.toByteArray(charset("UNICODE"))
            convertByteOrder(bList)
            bText = textInfo.toByteArray(charset("UNICODE"))
            convertByteOrder(bText)
            bFile = ByteArray(0)
            infoLength[0] = bList.size
            infoLength[1] = bText.size
            infoLength[2] = 0
            infoLength[3] = passwordBytes.size
            var allBytes = concatArrays(passwordBytes, bList, bText)
            if (_hideFileName.isNotEmpty()) {
                bFile = if (uriHide!!.path!!.startsWith("/document/")) {
                    FileService.readFileSelf(context, uriHide)
                } else {
                    FileService.readFile(uriHide!!.path)
                }
                if (bFile == null) {
                    return -3
                }
                allBytes = concatArrays(allBytes, bFile!!)
                infoLength[2] = bFile!!.size
                fileNameBytes = _hideFileName.toByteArray(charset("UNICODE"))
                convertByteOrder(fileNameBytes)
                infoLength[4] = fileNameBytes.size
            }
            allBytes = hashBytes(allBytes)
            if (saveInfo(
                    17,
                bmpByteArray,
                fileNameBytes,
                passwordBytes,
                bList,
                bText,
                bFile,
                allBytes,
            ) != 0 )
            {
                return -2
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
        return 0
    }

    companion object {
        init {
            System.loadLibrary("showInfo")
        }

        fun convertByteOrder(data: ByteArray) {
            if (data.size > 2 && data[0].toInt() == -2 && data[1].toInt() == -1) {
                var tmp: Byte
                var i = 0
                while (i < data.size) {
                    tmp = data[i + 1]
                    data[i + 1] = data[i]
                    data[i] = tmp
                    i += 2
                }
            }
        }
    }
}

fun setPercent(infoLength: Int) {
    val all: Float = (originalBMP!!.width * originalBMP!!.height * 3.0f - 500) / 8
    infoPercent = infoLength / all
}

fun hashBytes(input: ByteArray): ByteArray {
    return MessageDigest.getInstance("SHA-256").digest(input)
}
