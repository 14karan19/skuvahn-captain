package com.skuvahncaptain.utilities

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPref @Inject constructor(private val pref: SharedPreferences) {
    private inline fun edit(block: SharedPreferences.Editor.() -> Unit) {
//        pref.edit().apply(block)
        pref.edit().apply {
            block()
            apply() // ✅ this actually saves changes
        }
    }

    var phoneNumber: String
        set(value) = edit { putString(PreferenceConstants.Companion.phone, value) }
        get() = pref.getString(PreferenceConstants.Companion.phone, "") ?: ""

    var token: String
        set(value) = edit { putString(PreferenceConstants.Companion.token, value) }
        get() = pref.getString(PreferenceConstants.Companion.token, "") ?: ""

    var firebaseToken: String
        set(value) = edit { putString(PreferenceConstants.Companion.firebaseToken, value) }
        get() = pref.getString(PreferenceConstants.Companion.firebaseToken, "") ?: ""

    var userId: String
        set(value) = edit { putString(PreferenceConstants.Companion.userId, value) }
        get() = pref.getString(PreferenceConstants.Companion.userId, "") ?: ""

    var allowOffline: Boolean
        set(value) = edit { putBoolean(PreferenceConstants.Companion.allowOffline, value) }
        get() = pref.getBoolean(PreferenceConstants.Companion.allowOffline, false)

    var isLoggedIn: Boolean
        set(value) = edit { putBoolean(PreferenceConstants.Companion.isLoggedIn, value) }
        get() = pref.getBoolean(PreferenceConstants.Companion.isLoggedIn, false)

    var email: String
        set(value) = edit { putString(PreferenceConstants.Companion.email, value) }
        get() = pref.getString(PreferenceConstants.Companion.email, "") ?: ""

    var name: String
        set(value) = edit { putString(PreferenceConstants.Companion.name, value) }
        get() = pref.getString(PreferenceConstants.Companion.name, "") ?: ""

    var group: String
        set(value) = edit { putString(PreferenceConstants.Companion.group, value) }
        get() = pref.getString(PreferenceConstants.Companion.group, "") ?: ""

    var roleId: String
        set(value) = edit { putString(PreferenceConstants.Companion.roleId, value) }
        get() = pref.getString(PreferenceConstants.Companion.roleId, "") ?: ""

    var roleName: String
        set(value) = edit { putString(PreferenceConstants.Companion.roleName, value) }
        get() = pref.getString(PreferenceConstants.Companion.roleName, "") ?: ""

    var groupId: String
        set(value) = edit { putString(PreferenceConstants.Companion.groupId, value) }
        get() = pref.getString(PreferenceConstants.Companion.groupId, "") ?: ""

    var groupCode: String
        set(value) = edit { putString(PreferenceConstants.Companion.groupCode, value) }
        get() = pref.getString(PreferenceConstants.Companion.groupCode, "") ?: ""

    var profilePic: String
        set(value) = edit { putString(PreferenceConstants.Companion.profilePic, value) }
        get() = pref.getString(PreferenceConstants.Companion.profilePic, "") ?: ""

    var latitude: String
        set(value) = edit { putString(PreferenceConstants.Companion.latitude, value) }
        get() = pref.getString(PreferenceConstants.Companion.latitude, "0.0") ?: "0.0"

    var longitude: String
        set(value) = edit { putString(PreferenceConstants.Companion.longitude, value) }
        get() = pref.getString(PreferenceConstants.Companion.longitude, "0.0") ?: "0.0"

    fun clearAll() {
        pref.edit().clear().commit()
    }
}