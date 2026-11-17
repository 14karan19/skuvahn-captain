package com.skuvahncaptain.module.auth.otp

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class OTPRequestModel {
    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null

    @SerializedName("otp")
    @Expose
    var otp: String? = null
}

class OTPResponseModel {

    @SerializedName("data")
    @Expose
    var data: UserData? = null

    @SerializedName("returnStatus")
    @Expose
    var returnStatus: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("returnMessage")
    @Expose
    var returnMessage: List<String?>? = null

    class UserData {
        @SerializedName("userId")
        @Expose
        var userId: String? = null

        @SerializedName("groupCode")
        @Expose
        var groupCode: String? = null

        @SerializedName("access_token")
        @Expose
        var accessToken: String? = null

        @SerializedName("token_type")
        @Expose
        var tokenType: String? = null

        @SerializedName("expires_in")
        @Expose
        var expiresIn: Long? = null

        @SerializedName("crewId")
        @Expose
        var crewId: Int? = null

        @SerializedName("allowOffline")
        @Expose
        var allowOffline: Boolean? = null

        @SerializedName("roleName")
        @Expose
        var roleName: String? = null
    }
}
