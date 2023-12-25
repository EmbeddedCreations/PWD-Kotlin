package com.example.pwd_app.viewModel.mapActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.R
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.viewModel.login.Login
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel
    private  var markerList: List<MarkerInfo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        Log.d("MainActivity2", "onCreate method is called")

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(applicationContext)
        val homeRepository = HomeRepository(apiInterface, database, applicationContext)
        mapViewModel = ViewModelProvider(this,MapViewModelFactory(homeRepository))[MapViewModel::class.java]

        mapViewModel.schools.observe(this) { schoolList ->
            Log.d("MainActivity2", "Observer triggered with schoolList size: ${schoolList.size}")
            markerList = schoolList.map {
                MarkerInfo(
                    LatLng(it.location_lat!!.toDouble(), it.location_long!!.toDouble()),
                    it.school_name.toString(),
                    it.taluka.toString()
                )
            }
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this)
            val button = findViewById<Button>(R.id.button)
            button.setOnClickListener {
                val intent = Intent(
                    this,
                    Login::class.java
                ) // Replace MainActivity with the actual class of your main screen
                startActivity(intent)
                finish() // Optional: Close the current activity if needed
            }
            Log.d("data","end of data");
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
            Log.d("MarkerInfo", "Title: ${markerInfo.title}, LatLng: ${markerInfo.latLng}, Additional Info: ${markerInfo.additionalInfo}")

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

