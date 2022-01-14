package com.sokhorn.bgapplication.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.*
import com.sokhorn.bgapplication.services.worker.UploadWorker
import java.util.concurrent.TimeUnit


class LocationUpdateService : Service() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private val TAG = "LocationServices"

    override fun onCreate() {
        super.onCreate()
        initData()
    }

    //Location Callback
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val currentLocation: Location = locationResult.lastLocation

            Log.d(
                TAG,
                currentLocation.latitude.toString() + "," + currentLocation.longitude
            )

            val data = Data.Builder()
                .putString("lng", currentLocation.longitude.toString())
                .putString("lat", currentLocation.latitude.toString())
                .build()
            val workRequest = PeriodicWorkRequestBuilder<UploadWorker>(1, TimeUnit.SECONDS)
                .setInputData(data)
                .build()

            WorkManager.getInstance(this@LocationUpdateService).enqueue(workRequest)
            //Share/Publish Location
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        Log.d(TAG, "onStartCommand: work now ")
        return START_STICKY;
    }

    private fun startLocationUpdates() {
        locationRequest?.let {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            mFusedLocationClient!!.requestLocationUpdates(
                it,
                locationCallback, Looper.myLooper()!!
            )
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun initData() {
        locationRequest = LocationRequest.create()
        locationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    companion object {
        //region data
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000
    }
}