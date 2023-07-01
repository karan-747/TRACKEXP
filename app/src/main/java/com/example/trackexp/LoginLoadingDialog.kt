package com.example.trackexp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

class LoginLoadingDialog(private val myContext: Context, private val parent: ViewGroup) {

    private  var alertDialog:androidx.appcompat.app.AlertDialog? =null
    fun startLoginLoadingDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(myContext)
        builder.setView(LayoutInflater.from(myContext).inflate(R.layout.login_loading_dialog,parent,false))
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog?.show()
    }
    fun stopLoginLoadingDialog(){
        alertDialog?.dismiss()
    }

}