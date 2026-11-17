package com.skuvahncaptain.module.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skuvahncaptain.base.api.ApiInterface
import com.skuvahncaptain.utilities.AppPref
import com.skuvahncaptain.utilities.image.ImageCompressor
import com.skuvahncaptain.utilities.image.S3Uploader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : ViewModel() {

    val state = MutableLiveData<ProfileState>()

    fun saveCaptain(
        pref: AppPref,
        context: Context,
        request: SaveCaptainRequest,
        profileUri: Uri,
        documents: Map<String, Uri>
    ) {

        viewModelScope.launch {

            state.postValue(ProfileState.Loading)

            try {

                val compressed = ImageCompressor.compress(context, profileUri)
                val profileUrl = uploadImage(pref, context, compressed)

                // upload docs
                val docs = mutableListOf<Document>()

                for ((type, uri) in documents) {
                    val compressed = ImageCompressor.compress(context, uri)
                    val url = uploadImage(pref, context, compressed)
                    docs.add(Document(type, url))
                }

                val finalRequest = request.copy(
                    imageUrl = profileUrl,
                    documents = docs
                )

                val response = apiInterface.saveCaptain(pref.token, finalRequest)

                if (response.success && response.data != null) {

                    state.postValue(
                        ProfileState.Success(
                            response.data.captainId,
                            response.data.vehicleId
                        )
                    )

                } else {
                    state.postValue(
                        ProfileState.Error(response.message ?: "Failed")
                    )
                }

            } catch (e: Exception) {
                state.postValue(
                    ProfileState.Error(e.message ?: "Something went wrong")
                )
            }
        }
    }

    fun deleteAccount(
        pref: AppPref
    ) = viewModelScope.launch {

        state.value = ProfileState.Loading

        try {

            val response = apiInterface.deleteUser(
                pref.token,
                DeleteUserRequest(pref.userId)
            )

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true) {
                    state.value = ProfileState.DeleteSuccess
                } else {
                    state.value = ProfileState.Error(
                        body?.message ?: "Unable to delete account."
                    )
                }

            } else {
                state.value = ProfileState.Error(
                    "Server error: ${response.code()}"
                )
            }

        } catch (e: Exception) {
            state.value = ProfileState.Error(
                e.message ?: "Unexpected error occurred"
            )
        }
    }

    private suspend fun uploadImage(pref: AppPref,  context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            S3Uploader.uploadImage(pref, context, uri)
        }
    }
}