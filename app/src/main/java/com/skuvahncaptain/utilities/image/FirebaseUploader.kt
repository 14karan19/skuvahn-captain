package com.skuvahncaptain.utilities.image

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseUploader {

    private val storage = FirebaseStorage.getInstance().reference

    suspend fun upload(
        context: Context,
        uri: Uri,
        path: String
    ): String {
        // Ensure Firebase Auth is initialized and signed in
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            try {
                auth.signInAnonymously().await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return suspendCancellableCoroutine { cont ->
            val ref = storage.child(path)

            ref.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    ref.downloadUrl
                }
                .addOnSuccessListener {
                    cont.resume(it.toString())
                }
                .addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }
    }
}
