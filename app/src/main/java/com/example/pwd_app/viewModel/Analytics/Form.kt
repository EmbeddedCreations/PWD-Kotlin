package com.example.pwd_app.viewModel.Analytics

import android.os.Bundle
import com.example.pwd_app.R
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Form : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var textViewRatingFeedback: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        ratingBar = findViewById(R.id.ratingBar)
        textViewRatingFeedback = findViewById(R.id.textViewRatingFeedback)

        // Set a listener to the RatingBar
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            updateRatingFeedback(rating)
        }
    }

    private fun updateRatingFeedback(rating: Float) {
        // Update the TextView with dynamic text based on the rating
        val feedback = when (rating.toInt()) {
            1 -> "Poor"
            2 -> "Fair"
            3 -> "Average"
            4 -> "Good"
            5 -> "Excellent"
            else -> ""
        }
        textViewRatingFeedback.text = feedback
    }
}
