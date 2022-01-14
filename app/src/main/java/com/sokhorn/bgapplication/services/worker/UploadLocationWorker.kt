package com.sokhorn.bgapplication.services.worker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.android.gms.maps.model.LatLng
import com.sokhorn.bgapplication.R
import com.sokhorn.bgapplication.RealmConfig
import com.sokhorn.bgapplication.request.AppLocation
import com.sokhorn.bgapplication.view.notification.AppNotification
import com.sokhorn.bgapplication.viewmodels.FakeJsonViewModel
import java.text.SimpleDateFormat
import java.util.*


class UploadWorker(
    appContext: Context, workerParams: WorkerParameters,
) :
    Worker(appContext, workerParams) {

    private val TAG = "UploadLocationWorker"
    private val appNotification = AppNotification()
    private val appLocation = AppLocation(appContext)
    private val realmConfig = RealmConfig(appContext)
    private var lastLat: LatLng? = null

    @SuppressLint("SimpleDateFormat")
    override fun doWork(): Result {
        try {
            val lng = inputData.getString("lng")
            val lat = inputData.getString("lat")
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss.SSSXXX")
            val currentDate = sdf.format(Date())
            Log.d(
                TAG,
                "doWork: current date $currentDate ${lng} ${lat}"
            )
            if (lat != null) {
                lng?.let {
                    submitLocation(
                        lat = lat,
                        lng = it,
                        dateTime = currentDate
                    )
                }
            }
            appNotification.showNotification(
                applicationContext,
                applicationContext.getString(R.string.app_name),
                "Message:$currentDate " +
                        "lng $lng, lat:  $lat"
            )
            return Result.success(workDataOf("a" to "a"))
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun submitLocation(lat: String, lng: String, dateTime: String) {
        if (lastLat == null)
            lastLat = LatLng(lat.toDouble(), lng.toDouble())
        val l = Location("")
//        val l = LatLng(lat.toDouble(), lng.toDouble())
//        val lastLocationModel = LastLocationModel(
//            l,
//            time = dateTime
//        )
//        realmConfig.saveLastLocation(lastLocationModel)
        val fakeJsonViewModel = FakeJsonViewModel()
//        fakeJsonViewModel.postLocation(
//            applicationContext, lat = lat,
//            lng = lng, dateTime = dateTime
//        )


//        val locationRequest: LocationRequest = LocationRequest.create()
//        locationRequest.interval = LocationUpdateService.UPDATE_INTERVAL_IN_MILLISECONDS
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        val mFusedLocationClient: FusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(applicationContext)
////Location Callback
//        val locationCallback: LocationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//                val currentLocation: Location = locationResult.lastLocation
//                Log.d(
//                    "Locations",
//                    currentLocation.getLatitude().toString() + "," + currentLocation.getLongitude()
//                )
//                fakeJsonViewModel.postLocation(
//                    applicationContext, lat = currentLocation.latitude.toString(),
//                    lng = currentLocation.longitude.toString(), dateTime = dateTime
//                )
//                //Share/Publish Location
//            }
//        }
//
//        locationRequest.let {
//            if (ActivityCompat.checkSelfPermission(
//                    applicationContext,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    applicationContext,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                mFusedLocationClient.requestLocationUpdates(
//                    it,
//                    locationCallback, Looper.myLooper()!!
//                )
//                return
//            }
//        }


    }
}

//             appLocation.appLocation = object : LocationCallBack<Location> {
//                override fun locationCallBack(t: Location) {
//                    Log.d(TAG, "locationCallBack: location ${t.latitude} ${t.longitude}")
//                    appNotification.showNotification(
//                        applicationContext,
//                        applicationContext.getString(R.string.app_name),
//                        "Location : ${t.latitude} & ${t.longitude}"
//                    )
//                }
//            }
//            // Get the input
//            val imageUriInput = inputData.getString(Constants.KEY_IMAGE_URI)
//
//            // Do the work
//            val response = upload(imageUriInput)

//            // Create the output of the work
//            val imageResponse = response.body()
//            val imgLink = imageResponse.data.link
//            // workDataOf (part of KTX) converts a list of pairs to a [Data] object.
//            val outputData = workDataOf(Constants.KEY_IMAGE_URI to imgLink)
//
//            return Result.success(outputData)


