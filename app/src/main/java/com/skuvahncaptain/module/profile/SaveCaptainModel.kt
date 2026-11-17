package com.skuvahncaptain.module.profile

data class SaveCaptainRequest(
    val userId: String,
    val fullName: String,
    val phone: String,
    val email: String?,
    val imageUrl: String,
    val registrationNumber: String,
    val model: String,
    val color: String,
    val seatCapacity: Int,
    val insuranceExpiry: String,
    val fitnessExpiry: String,
    val pollutionExpiry: String,
    val documents: List<Document>
)

data class Document(
    val documentType: String,
    val fileUrl: String
)

data class SaveCaptainResponse(
    val captainId: String,
    val vehicleId: String
)