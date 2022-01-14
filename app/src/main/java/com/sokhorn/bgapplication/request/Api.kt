package com.sokhorn.bgapplication.request

import android.app.Activity
import android.app.Dialog
import com.nexvis.dconetv2.views.request.OnResponseListener
import com.nexvis.dconetv2.views.request.SingleCallbackWrapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

object Api {
    private var mActivity: Activity? = null
    private var mDialog: Dialog? = null
    fun init(activity: Activity) {
        mActivity = activity
    }

    fun <T> sendRequest(
        type: Any?,
        disposable: CompositeDisposable,
        observer: Single<T>,
        onResponseListener: OnResponseListener<T>
    ) {
        disposable.add(
            observer
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                    SingleCallbackWrapper(
                        mActivity,
                        type,
                        onResponseListener
                    ).apply {
                    onNewDialog = {
                        if (mActivity !=null && !mActivity!!.isDestroyed){
                            mDialog?.dismiss()
                            mDialog = it
                        }
                    }
                })
        )
    }
}