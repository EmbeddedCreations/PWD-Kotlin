package com.example.pwd_app.viewModel.buildingDisplay

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.R
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials

class BuildingDisplay : AppCompatActivity() {
    private lateinit var buildingDisplayViewModel: BuildingDisplayViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        buildingDisplayViewModel = ViewModelProvider(
            this,
            BuildingDisplayViewModelFactory(apiInterface)
        )[BuildingDisplayViewModel::class.java]
        // Find views by their IDs
        val schoolNameTextView = findViewById<TextView>(R.id.schoolNameTextView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Initialize the RecyclerView and its adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        buildingDisplayViewModel.fetchBuildingData()
        buildingDisplayViewModel.schoolList.observe(this) { buildings ->
            recyclerView.adapter = BuildingAdapter(buildings)
        }

        // Set school name and date
        schoolNameTextView.text = Credentials.SELECTED_SCHOOL_FOR_DISPLAY
    }
}