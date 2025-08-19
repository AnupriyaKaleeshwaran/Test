package com.example.productlisttestapp.apiutils

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavOptions
import com.google.gson.GsonBuilder

class DisplayLoader(val activity: Activity) {

    var loader = ProgressDialog(activity)

    init {

        loader.setMessage("Loading...")
        loader.setCancelable(false)
        //loader.show()
    }

    val gson = GsonBuilder().setLenient().create()
    fun startLoader() {
        if (!loader.isShowing) loader.show()
    }

    fun stopLoader() {
        if (loader.isShowing) loader.dismiss()
    }

    fun displayToastCenter(msg: String) {
        val toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun displayToastTop(msg: String) {
        val toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 100)
        toast.show()
    }

}

