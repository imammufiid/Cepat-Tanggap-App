package com.example.cepattanggapapp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.cepattanggapapp.utils.MySettingLocation
import com.google.android.gms.location.*

class MySimpleLocation(
    private val activity: Activity,
    mySimpleLocationCallback: MySimpleLocationCallback
) :
    MySettingLocation.MySettingLocationCallback {

    private val TAG = "MYSIMPLELOCATION"
    private var mLocationCallback: LocationCallback? = null

    private var mFusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    interface MySimpleLocationCallback {
        fun getLocation(location: Location)
    }

    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */

    private val locationRequest: LocationRequest
        get() {
            val locationRequest = LocationRequest()
            locationRequest.interval = 5000
            locationRequest.fastestInterval = 1000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            return locationRequest
        }

    init {
        // init location callback
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                p0?.lastLocation?.let {
                    mySimpleLocationCallback.getLocation(it)
                }
            }
        }
    }

    fun getMyLocation() {
        Log.d(TAG, "getMyLocation : Trying to get location")
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationProviderClient.requestLocationUpdates(
            locationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    fun checkLocationSetting(activity: Activity) {
        Log.d(TAG, "checkLocationSetting : Checking GPS or Highly Accurate is active or not")
        MySettingLocation.checkLocationSetting(locationRequest, activity, this)
    }

    fun stopGetLocation() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener {
                Log.d(TAG, "Location Callback was removed $it")
            }
    }

    override fun settingLocationActive(isActive: Boolean) {
        if (isActive) {
            Log.d(TAG, "settingLocationActive : GPS is active")
            getMyLocation()
        }
    }
}