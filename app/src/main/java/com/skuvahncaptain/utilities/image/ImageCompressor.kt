package com.skuvahncaptain.utilities.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.File

object ImageCompressor {

    fun compress(context: Context, uri: Uri): Uri {
        val bitmap =
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

        val file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it)
        }

        return file.toUri()
    }
}
