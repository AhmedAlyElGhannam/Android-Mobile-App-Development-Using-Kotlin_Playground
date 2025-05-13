package com.example.lab_02

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.renderscript.RenderScript
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var lat : TextView
    lateinit var lon : TextView
    lateinit var add : TextView
    lateinit var msgBtn : Button
    lateinit var mapBtn : Button
    lateinit var addressTxt : String
    private val LOCATION_PERMISSION_REQUESTCODE : Int = 1006
    private val LOCATION_PERMISSIONS : Array<String> = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        msgBtn.setOnClickListener {
            val message = "My address is $addressTxt"
            val phone = "07775000"
            val intent : Intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("message", message)
            intent.putExtra("phone", phone)
            startActivity(intent)
        }
        mapBtn.setOnClickListener {
            val message = "My address is $addressTxt"
            val phone = "07775000"
            val intent : Intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$phone"))
            intent.putExtra("sms_body", message)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (isLocationEnable()) {
                getFreshLocation()
            }
            else {
                enableLocationServices()
            }
        }
        else {
            ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS,LOCATION_PERMISSION_REQUESTCODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUESTCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkPermissions() : Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        return true
    }

    private fun enableLocationServices() {
        Toast.makeText(this , "Please Turn on Location" , Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun isLocationEnable() : Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getFreshLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permissions aren't granted, request them
            ActivityCompat.requestPermissions(
                this,
                LOCATION_PERMISSIONS,
                LOCATION_PERMISSION_REQUESTCODE
            )
            return
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.requestLocationUpdates(LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build(),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    lat.text = location?.latitude.toString()
                    lon.text = location?.longitude.toString()
                    val geocoder : Geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                    addressTxt = geocoder.getFromLocation(location?.latitude ?: 0.0, location?.longitude ?: 0.0, 1)
                        ?.get(0)
                        .toString()
                    add.text = addressTxt
                }},
                Looper.myLooper())
    }


    fun initUI() {
        lat = findViewById(R.id.latitudeTxt)
        lon = findViewById(R.id.longtudeTxt)
        add = findViewById(R.id.addresstxt)
        msgBtn = findViewById(R.id.button)
        mapBtn = findViewById(R.id.button2)
    }

    fun checkLocationPermissions() : Boolean {
        return (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                &&
                (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
}
