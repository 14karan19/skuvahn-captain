package com.skuvahncaptain.base.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.skuvahncaptain.base.listeners.BaseListener
import com.skuvahncaptain.utilities.AppPref
import com.skuvahncaptain.database.SkuVahnDatabase
import com.skuvahncaptain.network.NetworkController
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), BaseListener {

    @Inject
    lateinit var pref: AppPref

    private lateinit var viewBinding: V
    val binding get() = viewBinding

    var skuVahnDatabase: SkuVahnDatabase? = null

    private var currentLocation: Location? = null
    lateinit var locationManager: LocationManager

    private var locationByGps: Location? = null
    private var locationByNetwork: Location? = null

    private val networkController by lazy { NetworkController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )

        skuVahnDatabase = SkuVahnDatabase.getInstance(this)

        networkController.attach()
        init()
        initLocation()
    }

    private fun init() {
        viewBinding = DataBindingUtil.setContentView(this, getLayoutResourceId())
        setupInsets()
        initView()
    }

    private fun setupInsets() {

        ViewCompat.setOnApplyWindowInsetsListener(
            binding.root
        ) { view, insets ->

            val topInset =
                insets.getInsets(
                    WindowInsetsCompat.Type.statusBars()
                ).top

            view.setPadding(
                view.paddingLeft,
                topInset,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }
    }
    abstract fun getLayoutResourceId(): Int

    open fun initView() {}

    open fun containsOnlyFragment(): Boolean {
        return true
    }

    inline fun <reified T : Fragment> getFragment(): T? {
        val fragment = supportFragmentManager.findFragmentByTag(T::class.java.simpleName)
        return if (fragment != null) fragment as T else null
    }

    inline fun <reified T : AppCompatActivity> openA(extras: Bundle? = Bundle()) {
        openA(T::class, extras)
    }

    override fun openA(kClass: KClass<out AppCompatActivity>, extras: Bundle?) {
        val intent = Intent(this, kClass.java)
        if (extras != null) intent.putExtras(extras)
        this.startActivity(intent)
    }

    fun <T : ViewBinding> showDialog(
        clazz: KClass<out BaseDialogFragment<T>>,
        bundle: Bundle? = Bundle()
    ): BaseDialogFragment<T> {
        try {
            val tag = clazz.simpleName

            var fragment = supportFragmentManager.findFragmentByTag(tag)
            if (fragment != null) {
                val ft = supportFragmentManager.beginTransaction()
                ft.remove(fragment).commit()
            }
            val ft = supportFragmentManager.beginTransaction()
            ft.addToBackStack(tag)

            // Create and show the dialog.
            fragment = clazz.java.newInstance()
            fragment.arguments = bundle
            fragment.show(ft, tag)

            return fragment
        } finally {
        }
    }

//    override fun onResume() {
//        super.onResume()
//        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
//    }

    override fun onPause() {
        super.onPause()
//        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun initLocation() {
        try {
            if (isLocationPermissionGranted()) {
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                val gpsLocationListener: LocationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        locationByGps = location
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                val networkLocationListener: LocationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        locationByNetwork = location
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }

                if (hasGps) {
                    if (ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        0F,
                        gpsLocationListener
                    )
                }

                if (hasNetwork) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        0F,
                        networkLocationListener
                    )
                }

                val lastKnownLocationByGps =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                lastKnownLocationByGps?.let {
                    locationByGps = lastKnownLocationByGps
                }

                val lastKnownLocationByNetwork =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                lastKnownLocationByNetwork?.let {
                    locationByNetwork = lastKnownLocationByNetwork
                }

                if (locationByGps != null) {
                    currentLocation = locationByGps
                    pref.latitude = currentLocation!!.latitude.toString()
                    pref.longitude = currentLocation!!.longitude.toString()
                } else {
                    if (locationByNetwork != null) {
                        currentLocation = locationByNetwork
                        pref.latitude = currentLocation!!.latitude.toString()
                        pref.longitude = currentLocation!!.longitude.toString()
                        // use latitude and longitude as per your need
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA
                ), 22
            )
            false
        } else {
            true
        }
    }

}