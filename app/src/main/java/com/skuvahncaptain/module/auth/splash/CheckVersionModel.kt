package com.skuvahncaptain.module.auth.splash

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CheckVersionModel {

    @SerializedName("errors")
    @Expose
    var errors: List<Any?>? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("returnStatus")
    @Expose
    var returnStatus: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("returnMessage")
    @Expose
    var returnMessage: List<String?>? = null

    @Entity
    class Data : java.io.Serializable{

        @PrimaryKey(autoGenerate = true)
        var uid = 0

        @SerializedName("isSupported")
        @Expose
        var isSupported: Boolean? = null
    }
}