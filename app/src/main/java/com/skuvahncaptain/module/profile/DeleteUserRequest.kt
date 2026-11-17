package com.skuvahncaptain.module.profile

data class DeleteUserRequest(
    val userId: String
)

data class DeleteUserResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String?,
    val messages: List<String>?,
    val errors: Map<String, List<String>>?,
    val data: DeleteUserData?
)

data class DeleteUserData(
    val userId: String?
)