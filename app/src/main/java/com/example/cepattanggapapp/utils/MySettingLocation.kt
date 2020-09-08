package com.example.cepattanggapapp.utils

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

object MySettingLocation {
    private const val TAG = "MYSETTINGLOCATION"
    const val SETTING_CODE = 5000

    fun checkLocationSetting(
        locationRequest: LocationRequest,
        activity: Activity,
        mySettingLocationCallback: MySettingLocationCallback
    ) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener(activity) {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            Log.d(TAG, "onSuccessListener")
            mySettingLocationCallback.settingLocationActive(true)
        }
        task.addOnFailureListener(activity) {e ->
            if(e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    Log.d(TAG, "onFailureListener try ${e.localizedMessage}")
                    e.startResolutionForResult(activity, SETTING_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                    Log.d(TAG, "onFailureListener catch ${sendEx.localizedMessage}")
                }
            }
        }
    }
    interface MySettingLocationCallback {
        fun settingLocationActive(isActive: Boolean)
    }
}