package com.example.pwd_app.viewModel.analytics

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pwd_app.R
import com.example.pwd_app.viewModel.Analytics.PowerBI

class Analytics : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.activity_analytics, container, false)

        // Find the button by its ID
        val goButton = view.findViewById<Button>(R.id.gobutton)

        // Set click listener for the button
        goButton.setOnClickListener {
            // Create an Intent to navigate to the WorkLog class
            val intent = Intent(activity, PowerBI::class.java)

            // Start the WorkLog activity
            startActivity(intent)
        }

        return view
    }
}