package com.example.pwd_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pwd_app.viewModel.login.Login
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity2 : AppCompatActivity(), OnMapReadyCallback {
    private val markerList = listOf(
        MarkerInfo(LatLng(37.7749, -122.4194), "Marker 1 - San Francisco", "Additional Info 1"),
        MarkerInfo(LatLng(34.0522, -118.2437), "Marker 2 - Los Angeles", "Additional Info 2"),
        MarkerInfo(LatLng(40.7128, -74.0060), "Marker 3 - New York", "Additional Info 3")
        // Add more MarkerInfo objects as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, Login::class.java) // Replace MainActivity with the actual class of your main screen
            startActivity(intent)
            finish() // Optional: Close the current activity if needed
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        for (markerInfo in markerList) {
            googleMap.addMarker(
                MarkerOptions()
                    .position(markerInfo.latLng)
                    .title(markerInfo.title)
                    .snippet(markerInfo.additionalInfo)
            )
        }

        // Add map settings
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isZoomGesturesEnabled = true
        uiSettings.isRotateGesturesEnabled = true
        uiSettings.isTiltGesturesEnabled = true
    }
}

data class MarkerInfo(val latLng: LatLng, val title: String, val additionalInfo: String)

