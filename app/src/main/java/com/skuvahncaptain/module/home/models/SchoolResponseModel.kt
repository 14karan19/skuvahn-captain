package com.skuvahncaptain.module.home.models

data class SchoolResponseModel(
    val success: Boolean,
    val message: String?,
    val data: ArrayList<School>?
) {
    data class School(
        val id: String,
        val name: String
    )
}