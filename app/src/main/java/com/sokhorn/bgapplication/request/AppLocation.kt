package com.sokhorn.bgapplication.request

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.sokhorn.bgapplication.request.repository.ProductRepository


class AppLocation(private val context: Context) : LocationListener {
    private val TAG = "AppLocation"

    companion object {
        private var productRepository: ProductRepository? = null
        @Synchronized
        fun getInstance(): ProductRepository {
            if (productRepository == null) {
                productRepository = ProductRepository()
            }
            return productRepository!!
        }
    }

    var appLocation: LocationCallBack<Location>? = null
     fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                1000
            )
            return
        }
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val task = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            appLocation?.locationCallBack(location)
            Log.d(TAG, "fetchLocation: ${location.latitude} ${location.longitude}")
        }
    }

    override fun onLocationChanged(location: Location) {

    }
}

interface LocationCallBack<T> {
    fun locationCallBack(t: T)
}