package com.skuvahncaptain.utilities.image

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun getFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val file = File(context.cacheDir, "upload.jpg")
        file.outputStream().use { inputStream.copyTo(it) }
        return file
    }
}