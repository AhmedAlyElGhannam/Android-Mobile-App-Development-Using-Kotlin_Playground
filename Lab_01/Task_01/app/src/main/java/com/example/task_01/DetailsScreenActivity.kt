package com.example.task_01

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class DetailsScreenActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.second_activity)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProductDetailsFragment())
            .commit()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()

        val receivedIntent = intent
        val receivedProduct = receivedIntent.getSerializableExtra("product", Product::class.java)

        Log.i("TAG", "onCreate: $receivedProduct")

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as ProductDetailsFragment
        fragment.populateProductDetailsItems(receivedProduct)
    }
}