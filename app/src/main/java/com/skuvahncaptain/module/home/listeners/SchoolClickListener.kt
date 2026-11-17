package com.skuvahncaptain.module.home.listeners

import com.skuvahncaptain.module.home.models.SchoolResponseModel


interface SchoolClickListener {
    fun onSchoolClick(school: SchoolResponseModel.School)
}