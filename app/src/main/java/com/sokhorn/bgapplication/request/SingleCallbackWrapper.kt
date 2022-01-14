package com.nexvis.dconetv2.views.request

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonParseException
import com.sokhorn.bgapplication.view.DialogOnClick
import com.sokhorn.bgapplication.view.PTDialog
import com.sokhorn.bgapplication.R
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class SingleCallbackWrapper<T>(
    var activity: Activity? = null,
    var type: Any?,
    var onResponseListener: OnResponseListener<T>
) :
    DisposableSingleObserver<T>() {
    private val TAG = "SingleCallbackWrapper"
    var onNewDialog: ((AlertDialog?) -> (Unit))? = null
    override fun onSuccess(t: T) {
        onResponseListener.onResponseSuccess(type, t)
    }

    override fun onError(e: Throwable) {
        handleError(type, e, onResponseListener)
    }

    private fun <T> handleError(
        type: Any?,
        e: Throwable,
        onResponseListener: OnResponseListener<T>
    ) {
        var ptDialog: PTDialog? = null
        if (activity != null) {
            ptDialog = PTDialog(activity!!)
        }

        Log.e("TAG", "handleError: error with $e")

        when (e) {
            is com.jakewharton.retrofit2.adapter.rxjava2.HttpException -> {
                val responseBody = e.response()?.errorBody()
                Log.e("TAG", "handleError: http error $responseBody")
                Log.d("TAG", "handleError: retrofit error body $responseBody")
                onResponseListener.apply {
                    val errorMessage =
                        if (responseBody != null) getErrorMessage(responseBody)
                        else activity?.getString(R.string.please_check_your_connection_try_again)
                            ?: ""
                    when {
                        e.code() == 401 -> {
                            if (activity != null && !activity!!.isDestroyed) {
                                val dialog = ptDialog?.errorDialog(
                                    "Session expired",
                                    message = activity?.getString(R.string.your_acc_log_with_other_device)
                                        ?: "Session expired.",
                                    object : DialogOnClick {
                                        override fun onClick() {
//                                            EnterPhoneActivity.launch(activity as BaseActivity)
                                            activity?.clearSession()
                                            activity?.finish()
                                        }
                                    })
                                dialog?.setCancelable(false)
                                Toast.makeText(
                                    activity,
                                    activity?.getString(R.string.your_acc_log_with_other_device)
                                        ?: "Session expired.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            onResponseFailed(type, Throwable(errorMessage))
                            onResponseFailed(type, e, errorMessage)
                            onResponseFailed(type, Throwable(errorMessage), e.code())
                        }
                        e.code() == 500 -> {
                            if (activity != null && !activity!!.isDestroyed) {
                                val dialog = ptDialog?.errorDialog(
                                    "Session expired",
                                    message = activity?.getString(R.string.your_acc_log_with_other_device)
                                        ?: "Session expired.",
                                    object : DialogOnClick {
                                        override fun onClick() {
//                                            EnterPhoneActivity.launch(
//                                                activity as com.nexvis.sunfixmall.activities.BaseActivity
//                                            )
                                            activity?.clearSession()
                                            activity?.finish()
                                        }
                                    })
                                dialog?.setCancelable(false)
                                Toast.makeText(
                                    activity,
                                    activity?.getString(R.string.your_acc_log_with_other_device)
                                        ?: "Session expired.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            onResponseFailed(type, Throwable(errorMessage))
                            onResponseFailed(type, e, errorMessage)
                            onResponseFailed(type, Throwable(errorMessage), e.code())
                        }
                        else -> {
                            onResponseFailed(type, Throwable(errorMessage))
                            onResponseFailed(type, e, errorMessage)
                            onResponseFailed(type, Throwable(errorMessage), e.code())
                        }
                    }
                }
            }
            is HttpException -> {
                Log.d(TAG, "handleError: http ")
                val responseBody = e.response()?.errorBody()
                Log.e("TAG", "handleError: http error $responseBody")
                Log.d("TAG", "handleError: retrofit error body $responseBody")
                onResponseListener.apply {
                    val errorMessage =
                        if (responseBody != null) getErrorMessage(responseBody)
                        else activity?.getString(R.string.please_check_your_connection_try_again)
                            ?: ""
                    if (e.code() == 401 || e.code() == 500) {
                        if (activity != null && !activity!!.isDestroyed) {
                            val dialog = ptDialog?.errorDialog(
                                "Session expired",
                                message = activity?.getString(R.string.your_acc_log_with_other_device)
                                    ?: "Session expired.",
                                object : DialogOnClick {
                                    override fun onClick() {
//                                        EnterPhoneActivity.launch(activity as BaseActivity)
                                        activity?.clearSession()
                                        activity?.finish()
                                    }
                                })
                            dialog?.setCancelable(false)
                            //                            val mDialog = activity?.showMessage(
                            //                                activity?.getString(R.string.your_acc_log_with_other_device)
                            //                                    ?: "Session expired."
                            //                            ) {
                            //                                activity?.clearSession()
                            //                                activity?.finish()
                            //                            }
                            //
                            //                            mDialog?.setCancelable(false)
                            //                            onNewDialog?.invoke(mDialog)
                            Toast.makeText(
                                activity,
                                activity?.getString(R.string.your_acc_log_with_other_device)
                                    ?: "Session expired.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        onTokenExpired(type, Throwable(errorMessage), errorMessage)
                    } else {
                        onResponseFailed(type, Throwable(errorMessage))
                        onResponseFailed(type, e, errorMessage)
                        onResponseFailed(type, Throwable(errorMessage), e.code())
                    }
                }
            }
            is SocketTimeoutException -> {
                onResponseListener.apply {
                    val textError =
                        activity?.getString(R.string.time_out_please_check_your_connection_try_again)
                            ?: "Unknown"
                    onResponseFailed(
                        type,
                        Throwable(textError)
                    )
                    onResponseFailed(
                        type,
                        e,
                        textError
                    )
                    onResponseFailed(
                        type,
                        Throwable(textError),
                        -1
                    )
                }
            }
            is IOException -> {
                onResponseListener.apply {
                    val textError =
                        activity?.getString(R.string.please_check_your_connection_try_again)
                            ?: "Unknown"
                    onResponseFailed(
                        type,
                        Throwable(textError)
                    )
                    onResponseFailed(type, e, textError)
                    onResponseFailed(
                        type,
                        Throwable(textError),
                        -1
                    )

                }
            }
            is JsonParseException -> {
                onResponseListener.apply {
                    onResponseFailed(type, Throwable("${e.message}"))
                    onResponseFailed(type, e, "${e.message}")
                    onResponseFailed(type, Throwable("${e.message}"), -1)

                }
            }
            else -> onResponseListener.apply {
                onResponseFailed(type, e)
                onResponseFailed(type, e, "${e.message}")
                onResponseFailed(type, Throwable("${e.message}"), -1)
            }
        }

    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getJSONObject("error").getString("message")
        } catch (e: Exception) {
            Log.d("TAG", "getErrorMessage: e ${e.message}")
            Log.d("Error", "${e.message}")
            activity?.getString(R.string.something_went_wrong_try_again_later)?: "Unknown"
        }
    }
}

fun Activity.clearSession() {
    //    EnterPhoneActivity.launchClearTask(this)
}

//private var popUpDialog: AlertDialog? = null
//fun Context.showMessage(message: String, onClickDone: ((Any?) -> Unit)? = null): AlertDialog {
//    try {
//        popUpDialog?.dismiss()
//    } catch (e: java.lang.Exception) {
//        e.printStackTrace()
//    }
//    val alertDialog = AlertDialog.Builder(this).create()
//    val popupView = LayoutInflater.from(this).inflate(R.layout.view_pop_up_message, null)
//    popupView.tv_pop_up_message.text = message
//    popupView.tv_pop_up_ok.setOnClickListener {
//        alertDialog?.dismiss()
//        onClickDone?.invoke("")
//    }
//    alertDialog.setView(popupView)
//    popUpDialog = alertDialog
//    popUpDialog?.show()
//    return alertDialog
//}


