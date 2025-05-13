package com.example.task_01

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), Communicator {
    private var fragment : ProductDetailsFragment? = null

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ListOfProductsFragments())
            .commit()
    }

    private fun switchToFragDetails() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container2, fragment!!, "fragment2")
            .commit()
        supportFragmentManager.executePendingTransactions()
    }

    private fun switchToListOfProducts(product : Product) {
        val intent = Intent(this, DetailsScreenActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    override fun showProductDetails(product: Product) {
        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
            if (fragment == null) {
                fragment = ProductDetailsFragment()
                switchToFragDetails()
            }
            fragment?.populateProductDetailsItems(product)
        }
        else if (resources.configuration.orientation == ORIENTATION_PORTRAIT) {
            switchToListOfProducts(product)
        }
    }
}