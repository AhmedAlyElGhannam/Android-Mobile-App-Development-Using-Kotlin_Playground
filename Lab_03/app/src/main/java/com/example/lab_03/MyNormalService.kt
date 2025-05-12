package com.example.lab_03

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyNormalService : Service() {
    private val myBinder : IBinder = MyLocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    fun getCurrentTime() : String {
        val dateFormat = SimpleDateFormat("HH:mm:ss mm/dd/yyyy", Locale.US)
        return dateFormat.format(Date())
    }

    inner class MyLocalBinder : Binder() {
        fun getService() : MyNormalService {
            return this@MyNormalService
        }
    }
}