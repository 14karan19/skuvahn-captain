package com.skuvahncaptain.utilities

enum class DriverDocType {
    PROFILE,
    DL_FRONT,
    DL_BACK,
    AADHAAR_FRONT,
    AADHAAR_BACK,
    RC,
    INSURANCE
}

class PreferenceConstants {
    companion object {
        const val phone = "phone"
        const val token = "token"
        const val firebaseToken = "firebaseToken"
        const val userId = "userId"
        const val allowOffline = "allowOffline"
        const val address = "address"
        const val isLoggedIn = "isLoggedIn"
        const val email = "email"
        const val name = "name"
        const val group = "group"
        const val roleId = "roleId"
        const val roleName = "roleName"
        const val groupId = "groupId"
        const val profilePic = "profilePic"
        const val groupCode = "groupCode"

        const val editAddress = "editAddress"

        const val latitude = "latitude"
        const val longitude = "longitude"

        const val child = "child"
        const val deleteUser = "deleteUser"
    }
}

class ApiConstants {
    companion object {
        const val something_wrong = "Something went wrong! Please try again later"
        const val command_already_processed = "Command has been processed already."
        const val something_wrong_command = "Something wrong while executing the command."
    }
}

class EndPoints {
    companion object {
        const val baseUrl = "https://api.skuvahn.com/api/v1/"
        const val sendOtp = "auth/sendOtp"
        const val resendOtp = "auth/resendOtp"
        const val verifyOtp = "auth/verifyOtp"
        const val saveCaptain = "users/saveCaptain"
        const val myRoutes = "mobile/myRoutes"
        const val deleteRoute = "mobile/routes/{id}"
        const val updateBooking = "mobile/bookings/{id}"
        const val bookings = "mobile/captainBookings"
        const val routes = "mobile/routes"
        const val home = "mobile/home"
        const val schools = "mobile/schools"
        const val deleteUser = "users/deleteUser"
    }
}

class DateConstants {
    companion object {
        const val dateFormat1 = "yyyy-MM-dd'T'HH:mm:ss.SSXXX"
        const val dateFormat2 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"
        const val outputFormat = "dd-MM-yyyy"
    }
}