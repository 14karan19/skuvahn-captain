package com.skuvahncaptain.utilities.image

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.*
import java.util.Locale

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun fetchPincode(
        context: Context,
        onResult: (String?) -> Unit
    ) {
        if (!hasLocationPermission(context)) {
            onResult(null)
            return
        }

        val client = LocationServices.getFusedLocationProviderClient(context)

        val request = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            5_000
        )
            .setMaxUpdates(1)
            .build()

        client.requestLocationUpdates(
            request,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    client.removeLocationUpdates(this)

                    val location = result.lastLocation
                    if (location != null) {
                        resolvePincode(context, location.latitude, location.longitude, onResult)
                    } else {
                        fetchLastKnown(client, context, onResult)
                    }
                }
            },
            context.mainLooper
        )
    }


    @SuppressLint("MissingPermission")
    private fun fetchLastKnown(
        client: FusedLocationProviderClient,
        context: Context,
        onResult: (String?) -> Unit
    ) {
        client.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    resolvePincode(context, location.latitude, location.longitude, onResult)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    private fun resolvePincode(
        context: Context,
        lat: Double,
        lng: Double,
        onResult: (String?) -> Unit
    ) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder.getFromLocation(lat, lng, 1)
                ?.firstOrNull()

            val pincode = address?.postalCode
            onResult(pincode)
        } catch (e: Exception) {
            onResult(null)
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        return fine || coarse
    }
}
