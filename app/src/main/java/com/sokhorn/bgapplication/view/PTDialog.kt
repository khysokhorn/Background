package com.sokhorn.bgapplication.view

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sokhorn.bgapplication.R


class PTDialog(private val context: Context) {
    fun errorDialog(
        title: String = "Session expired.",
        message: String = context.getString(R.string.your_acc_log_with_other_device),
        itemOnclick: DialogOnClick
    ): Dialog {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(
            "ok"
        ) { p0, p1 ->
            itemOnclick.onClick()
        }
        dialog.show()
        return dialog.create()
    }

    fun confirmDialog(message: Int, itemOnclick: DialogOnClick) {
        val dialog = MaterialAlertDialogBuilder(context)
        dialog.setMessage(context.getString(message))
        val d = dialog.create()
//        d.window?.setWindowAnimations(R.style.DialogAnimationScaleInOut)
        d.window?.setDimAmount(.5f)
        dialog.setPositiveButton(
         "ok"
        ) { p0, p1 ->
            itemOnclick.onClick()
            dialog.create().dismiss()
        }
        dialog.setNegativeButton(
          "cancel"
        ) { p0, p1 -> dialog.create().dismiss() }

        if (!dialog.show().isShowing)
            dialog.show()
    }

    fun progressDialog(
        message: Int? = null,
        isCancelable: Boolean = false
    )  : Dialog{
        val alertDialogBuilder = AppCompatDialog(context)


        alertDialogBuilder.setCancelable(isCancelable)

        if (!alertDialogBuilder.isShowing)
            alertDialogBuilder.show()
        return alertDialogBuilder
    }

}