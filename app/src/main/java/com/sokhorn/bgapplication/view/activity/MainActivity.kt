package com.sokhorn.bgapplication.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nexvis.sunfixmall.request.repo.State
import com.sokhorn.bgapplication.services.LocationUpdateService
import com.sokhorn.bgapplication.R
import com.sokhorn.bgapplication.databinding.ActivityMainBinding
import com.sokhorn.bgapplication.request.AppLocation
import com.sokhorn.bgapplication.view.notification.AppNotification
import com.sokhorn.bgapplication.viewmodels.FakeJsonViewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // request permission location
        requestLocationPermission()

        val appNotification = AppNotification()
        val fakeJsonViewModel = FakeJsonViewModel()
        val application = AppLocation(this)
        application.fetchLocation()

        val i = Intent(applicationContext, LocationUpdateService::class.java)
        startService(i)
        binding.btn.setOnClickListener {
            appNotification.showNotification(
                this, this.getString(R.string.app_name), "New Message"
            )
        }
        fakeJsonViewModel.exploreModelLiveData.observe(this, {
            when (it.status) {
                State.PROGRESS -> {
                }
                State.ERROR -> {
                }
                State.SUCCESS -> {
                    Log.d(TAG, "onCreate: ${it.data}")
                }
                State.NONE -> {
                }
            }
        })
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    fun requestLocationPermission() {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        } else {
//          EasyPermissions.requestPermissions(
//              this,
//              "Please grant the location permission",
//              "REQUEST_LOCATION_PERMISSION",
//              perms
//          )
            EasyPermissions.requestPermissions(
                this, "", REQUEST_LOCATION_PERMISSION, *perms
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }
}