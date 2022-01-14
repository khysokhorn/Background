package com.sokhorn.bgapplication

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmConfig(val context: Context) {

    private val realmName: String = context.getString(R.string.app_name) + "Realm"
    private val config = RealmConfiguration.Builder().name(realmName)
        .build()
    private val backgroundThreadRealm: Realm = Realm.getInstance(config)

//    fun saveLastLocation(lastLocationModel: LastLocationModel) {
//        backgroundThreadRealm.executeTransaction {
//            it.insertOrUpdate(lastLocationModel)
//        }
//    }
//
//    fun getLastLocation(): RealmResults<LastLocationModel> = backgroundThreadRealm.where<LastLocationModel>().findAllAsync()


}