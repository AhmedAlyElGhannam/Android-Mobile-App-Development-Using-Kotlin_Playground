package com.example.lab_03

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.IOException

class DisplayImage : AppCompatActivity() {
    lateinit var imgView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        imgView = findViewById(R.id.imgView)


        try {
            val imgPath = intent.getStringExtra("path_img")

            val file = imgPath?.let { File(it) }

            val bitmap = BitmapFactory.decodeFile(file?.absolutePath ?: "")

            imgView.setImageBitmap(bitmap)

        } catch (_: IOException) {

        }
    }
}