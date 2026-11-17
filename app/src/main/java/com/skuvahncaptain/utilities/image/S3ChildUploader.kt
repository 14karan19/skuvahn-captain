package com.skuvahncaptain.utilities.image

import android.content.Context
import android.net.Uri
import com.amazonaws.services.s3.model.PutObjectRequest
import java.util.UUID

object S3ChildUploader {

    fun uploadImage(context: Context, fileUri: Uri): String {
        val file = FileUtils.getFile(context, fileUri)
        val fileName = "children/${UUID.randomUUID()}.jpg"

        S3ClientProvider.get(context).putObject(
            PutObjectRequest(S3ClientProvider.BUCKET_NAME, fileName, file)
        )

        return S3ClientProvider.publicUrl(fileName)
    }
}
