package com.sokhorn.bgapplication.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import java.io.Serializable
import java.util.*


public data class LastLocationModel(
    val distance: LatLng,
    val time: String
) : Serializable