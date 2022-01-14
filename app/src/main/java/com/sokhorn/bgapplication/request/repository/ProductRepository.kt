package com.sokhorn.bgapplication.request.repository


import com.sokhorn.bgapplication.request.Api
import com.nexvis.dconetv2.views.request.OnResponseListener
import com.nexvis.dconetv2.views.request.repository.ProductInterface
import com.sokhorn.bgapplication.models.RequestModel
import com.sokhorn.bgapplication.request.ApiClient
import io.reactivex.disposables.CompositeDisposable

class ProductRepository {

    private var productInterface =
        ApiClient.getClient().create(ProductInterface::class.java)

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

    fun getFakeJson(
        lat: String,
        lng: String,
        dateTime: String,
        customerName: String,
        disposable: CompositeDisposable,
        onResponseListener: OnResponseListener<Boolean>
    ) {
        val requestBody =RequestModel(

            latitude = lat,
            longitude = lng,
            datetime = dateTime,
            contactName = customerName
        )
        val request = productInterface.postLocation(requestBody)
        Api.sendRequest("auth", disposable, request, onResponseListener)
    }

    fun a( lat: String,
           lng: String,){
         var productInterface =
            ApiClient.getClient().create(ProductInterface::class.java)
        val requestBody =RequestModel(
            latitude = lat,
            longitude = lng
        )
        val request = productInterface.postLocation(requestBody)

    }

}