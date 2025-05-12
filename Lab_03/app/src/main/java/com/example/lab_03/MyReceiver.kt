package com.example.lab_03

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeIntentLaunch")
    override fun onReceive(context: Context, intent: Intent) {
        val path = intent.getStringExtra("file_path")
        Log.i("TAG", "onReceive: $path")
        val actIntent = Intent(context, DisplayImage::class.java).apply {
            putExtra("path_img", path)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(actIntent)
    }
}