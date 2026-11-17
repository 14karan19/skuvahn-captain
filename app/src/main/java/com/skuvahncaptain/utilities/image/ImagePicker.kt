package com.skuvahncaptain.utilities.image

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File

object ImagePicker {

    fun createChooser(context: Context, cameraUri: Uri): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        }

        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        return Intent.createChooser(galleryIntent, "Select Image").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }
    }

    fun createCameraUri(context: Context): Uri {
        val file = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    fun extractResult(
        context: Context,
        resultIntent: Intent?,
        cameraUri: Uri
    ): Uri? {
        return resultIntent?.data ?: cameraUri
    }
}
