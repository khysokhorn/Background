package com.sokhorn.bgapplication.models


import com.google.gson.annotations.SerializedName

//"contactName": "string",
//  "datetime": "string"
data class RequestModel(
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("contactName")
    val contactName: String = "",
    @SerializedName("datetime")
    val datetime: String = "",
)