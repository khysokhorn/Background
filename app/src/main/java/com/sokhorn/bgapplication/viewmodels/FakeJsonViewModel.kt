package com.sokhorn.bgapplication.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexvis.dconetv2.viewmodels.BaseViewModel
import com.nexvis.dconetv2.views.request.OnResponseListener
import com.nexvis.sunfixmall.request.repo.Resource
import com.sokhorn.bgapplication.Utils
import com.sokhorn.bgapplication.request.repository.ProductRepository

class FakeJsonViewModel : BaseViewModel() {
    private val adRepository = ProductRepository()
    private val TAG = "FakeJsonViewModel"
    private val _exploreAdStatus = MutableLiveData<Resource<Boolean>>()
    val exploreModelLiveData: LiveData<Resource<Boolean>> = _exploreAdStatus

    fun postLocation(
        context: Context,
        lat: String,
        lng: String,
        dateTime: String,
    ) {
        Resource.progress()
        Log.d(TAG, "postLocation: request are in viewmodel $lat, ${lng}, $dateTime")
        adRepository.getFakeJson(
            lat = lat,
            lng = lng, dateTime = dateTime,
            customerName = Utils.getAndroidId(context),
            disposable = disposable,
            onResponseListener = object : OnResponseListener<Boolean> {
                override fun onResponseSuccess(type: Any?, data: Boolean?) {
                    Log.d(TAG, "onResponseSuccess:  $data")
                    _exploreAdStatus.value = Resource.success(data)
                }
            }
        )
    }
}