package com.nexvis.dconetv2.views.request.repository


import com.sokhorn.bgapplication.models.RequestModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


//{
//  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//  "latitude": "string",
//  "longitude": "string"
//}

interface ProductInterface {
    @POST("/api/app/location-tracker/set-location")
    fun postLocation(
        @Body body: RequestModel
    ): Single<Boolean>
}

