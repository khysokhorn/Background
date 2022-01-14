package com.nexvis.dconetv2.views.request

interface   OnResponseListener<in T> {
    fun onResponseSuccess(type: Any?, data: T?)
    fun onResponseFailed(type: Any?, t: Throwable){}
    fun onResponseFailed(type: Any?, t: Throwable, code: Int){
    }
    fun onResponseFailed(type: Any?, t: Throwable, errorMessage: String){

    }
    fun onTokenExpired(type: Any?, t: Throwable, errorMessage: String){}
}