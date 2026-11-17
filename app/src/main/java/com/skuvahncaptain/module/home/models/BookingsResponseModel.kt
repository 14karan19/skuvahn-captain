package com.skuvahncaptain.module.home.models

import java.io.Serializable

data class BookingsResponseModel(
    val success: Boolean,
    val statusCode: Int,
    val message: String?,
    val messages: List<String>?,
    val errors: Map<String, List<String>>?,
    val data: List<Booking>?
) : Serializable {

    data class Booking(
        val id: String,
        val status: Int,
        val createdAt: String,
        val child: Child,
        val school: School,
        val address: Address,
        val route: Route,
        val captain: Captain?,
        val vehicle: Vehicle?
    ) : Serializable

    data class Child(
        val id: String,
        val fullName: String,
        val dob: String,
        val grade: String,
        val imageUrl: String?,
        val emergencyContactName: String,
        val emergencyContactPhone: String
    ) : Serializable

    data class School(
        val id: String,
        val name: String,
        val address: String?,
        val pincode: String?,
        val landmark: String?,
        val latitude: String?,
        val longitude: String?,
        val contactPhone: String?,
        val contactEmail: String?
    ) : Serializable

    data class Address(
        val id: String,
        val latitude: String,
        val longitude: String,
        val locality: String,
        val fullAddress: String,
        val landmark: String?,
        val pincode: String?,
        val addressType: String
    ) : Serializable

    data class Route(
        val id: String,
        val routeStop: String,
        val landmark: String?,
        val latitude: String?,
        val longitude: String?,
        val isActive: Boolean
    ) : Serializable

    data class Captain(
        val id: String,
        val fullName: String,
        val phone: String,
        val email: String?,
        val imageUrl: String?
    ) : Serializable

    data class Vehicle(
        val id: String,
        val registrationNumber: String,
        val model: String,
        val color: String,
        val seatCapacity: Int,
        val rcNumber: String,
        val insuranceExpiry: String,
        val fitnessExpiry: String,
        val pollutionExpiry: String,
        val isActive: Boolean
    ) : Serializable
}