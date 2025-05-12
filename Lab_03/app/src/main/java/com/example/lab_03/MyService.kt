package com.example.lab_03

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MyService : Service() {

    val CHANNEL_ID = "CHANNEL_ID"
    lateinit var urlStr : String
    lateinit var receiver: MyReceiver



    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val intentFilter : IntentFilter = IntentFilter("broadcastSender")
        receiver = MyReceiver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            urlStr = intent.getStringExtra("url").toString()
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        val notification = builder.build()

        startForeground(1006, notification)
        Thread {
            // download image
            val bitmap : Bitmap? = download(urlStr)
            // save image
            val file = saveImageToInternalStorage(bitmap)
            // broadcast done signal
            val intent = Intent()
            intent.action = "broadcastSender"
            if (file != null) {
                intent.putExtra("file_path", file.path)
                Log.i("TAG", "onStartCommand: ${file.path}")
            }
            sendBroadcast(intent)

            // stop service
            stopSelf()
        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    @Throws(IOException::class)
    private fun download(url: String): Bitmap? {
        val imgurl = URL(url)
        val connection = imgurl.openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val responseCode = connection.responseCode
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val `is` = connection.inputStream
            return BitmapFactory.decodeStream(`is`)
        } else {
            return null
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name : String = "Service Running"
            val description : String = "Service is active."
            val importance : Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap?): File? {
        return try {
            // generate filename
            val filename = "image_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, filename)

            // write bitmap to file
            if (bitmap != null) {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
            }
            file
        } catch (e: IOException) {
            Log.e("DownloadService", "Failed to save image", e)
            null
        }
    }
}