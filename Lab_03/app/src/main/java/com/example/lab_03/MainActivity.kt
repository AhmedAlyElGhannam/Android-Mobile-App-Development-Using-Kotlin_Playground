package com.example.lab_03

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentFilter

class MainActivity : AppCompatActivity() {

    lateinit var btnDownload : Button
    lateinit var btnDate : Button
    lateinit var dateTxt : TextView
    lateinit var urlTxt : EditText
    lateinit var myNormalService: MyNormalService
    var isBound : Boolean = false

    private var myConnection : ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder : MyNormalService.MyLocalBinder = service as MyNormalService.MyLocalBinder
            myNormalService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDate = findViewById(R.id.showDateBtn)
        btnDownload = findViewById(R.id.downloadBtn)
        dateTxt = findViewById(R.id.dateTxt)
        urlTxt = findViewById(R.id.urlTxt)

        val intent = Intent(this, MyNormalService::class.java)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)

        btnDate.setOnClickListener {
            showTime()
        }

        btnDownload.setOnClickListener {
            val intent = Intent(this@MainActivity, MyService::class.java)
            intent.putExtra("url", urlTxt.text.toString())
            startForegroundService(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun showTime() {
        val currentTime : String = myNormalService.getCurrentTime()
        dateTxt.text = currentTime
    }
}