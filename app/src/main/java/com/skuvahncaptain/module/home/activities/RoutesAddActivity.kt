package com.skuvahncaptain.module.home.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hes.vayu.utilities.CommonUtils.hideLoader
import com.hes.vayu.utilities.CommonUtils.showLoader
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityAddRouteBinding
import com.skuvahncaptain.module.home.models.RouteAddRequest
import com.skuvahncaptain.module.home.models.SchoolResponseModel
import com.skuvahncaptain.module.home.states.RouteState
import com.skuvahncaptain.module.home.viewmodels.RoutesViewModel
import com.skuvahncaptain.utilities.CustomToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class RoutesAddActivity : BaseActivity<ActivityAddRouteBinding>(),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val viewModel: RoutesViewModel by viewModels()

    private var selectedLatLng: LatLng? = null
    private var selectedAddress = ""
    private var pinCode = ""
    private var city = ""
    private var schools = ArrayList<SchoolResponseModel.School>()
    private var selectedSchoolId: String? = null

    private lateinit var placesClient: PlacesClient
    private lateinit var schoolAutoAdapter: ArrayAdapter<String>

    override fun getLayoutResourceId() = R.layout.activity_add_route

    override fun initView() {
        super.initView()

        window.statusBarColor = getColor(R.color.light_blue)

        setupBottomSheet()

        // Init Places
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        attachClicks()
        setupSearch()
        observe()

        viewModel.loadBookingDropdowns(pref.token, pref.userId)
    }

    // ------------------ CURRENT LOCATION ------------------
    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            }
        }
    }

    // ------------------ SEARCH ------------------
    private fun setupSearch() {

        binding.etSearch.setOnEditorActionListener { _, _, _ ->

            val query = binding.etSearch.text.toString()
            if (query.isEmpty()) return@setOnEditorActionListener false

            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->

                    if (response.autocompletePredictions.isNotEmpty()) {
                        val prediction = response.autocompletePredictions[0]
                        getLatLngFromPlace(prediction)
                    }
                }

            true
        }
    }

    private fun getLatLngFromPlace(prediction: AutocompletePrediction) {

        val placeId = prediction.placeId

        val request = com.google.android.libraries.places.api.net.FetchPlaceRequest.builder(
            placeId,
            listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG)
        ).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val latLng = response.place.latLng
                latLng?.let {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
                }
            }
    }

    // ------------------ MAP ------------------
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getCurrentLocation()

        mMap.setOnCameraIdleListener {
            val center = mMap.cameraPosition.target
            selectedLatLng = center
            fetchAddress(center)
        }
    }

    // ------------------ ADDRESS ------------------
    private fun fetchAddress(latLng: LatLng) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (!list.isNullOrEmpty()) {
                selectedAddress = list[0].getAddressLine(0)
                binding.tvAddress.text = selectedAddress
                pinCode = list[0].postalCode ?: ""
                city = list[0].locality ?: ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ------------------ BUTTONS ------------------
    private fun attachClicks() {

        binding.btnSave.setOnClickListener {

            if (!validate()) return@setOnClickListener

            val request = RouteAddRequest(
                routeStop = binding.etRouteStop.text.toString().ifBlank { "" },
                latitude = selectedLatLng!!.latitude.toString(),
                longitude = selectedLatLng!!.longitude.toString(),
                address = binding.etAddress.text.toString().ifBlank { "" },
                landmark = binding.etLandmark.text.toString().ifBlank { "" },
                pincode = pinCode,
                schoolId = selectedSchoolId!!,
                captainId = pref.userId
            )

            viewModel.addRoute(pref.token, request)
        }
    }

    private fun validate(): Boolean {

        if (selectedLatLng == null) {
            CustomToast.showMessage(this, "Location not selected")
            return false
        }

        if (binding.etRouteStop.text.isNullOrBlank()) {
            CustomToast.showMessage(this, "Enter route stop")
            return false
        }

        if (binding.etAddress.text.isNullOrBlank()) {
            CustomToast.showMessage(this, "Enter address")
            return false
        }

        if (selectedSchoolId == null) {
            CustomToast.showMessage(this, "Select school")
            return false
        }

        return true
    }

    private fun observe() {

        viewModel.state.observe(this) {

            when (it) {

                is RouteState.Loading -> showLoader(this)

                is RouteState.AddSuccess -> {
                    hideLoader()
                    CustomToast.showMessage(this, it.message)
                    setResult(RESULT_OK)
                    finish()
                }

                is RouteState.DropdownData -> {
                    hideLoader()
                    schools = it.schools

                    val schoolNames = schools.map { school ->
                        school.name
                    }

                    schoolAutoAdapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        schoolNames
                    )

                    binding.actvSchool.setAdapter(schoolAutoAdapter)

                    binding.actvSchool.setOnClickListener {
                        binding.actvSchool.showDropDown()
                    }

                    binding.actvSchool.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            binding.actvSchool.showDropDown()
                        }
                    }

                    binding.actvSchool.setOnItemClickListener { _, _, position, _ ->

                        val selectedSchool = schools[position]

                        selectedSchoolId = selectedSchool.id

                        binding.actvSchool.setText(
                            selectedSchool.name,
                            false
                        )
                    }
                }

                is RouteState.Error -> {
                    hideLoader()
                    CustomToast.showMessage(this, it.message)
                }

                else -> {}
            }
        }
    }

    private fun setupBottomSheet() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.peekHeight = 300
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}