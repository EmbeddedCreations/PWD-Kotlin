package com.example.pwd_app.viewModel.analytics

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pwd_app.R
import com.example.pwd_app.viewModel.Analytics.PowerBI

class Form : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var textViewRatingFeedback: TextView
    private lateinit var editTextAnswer: EditText
    private lateinit var submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        ratingBar = findViewById(R.id.ratingBar)
        textViewRatingFeedback = findViewById(R.id.textViewRatingFeedback)
        editTextAnswer = findViewById(R.id.editTextAnswer)
        submit = findViewById(R.id.buttonSubmit)

        // Set a listener to the RatingBar
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            updateRatingFeedback(rating)
        }

        // Set up TextWatcher for EditText
        editTextAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                // Check if the entered text is not a number
                if (!editable.toString().matches(Regex("\\d*"))) {
                    // Show warning or change text color to red
                    editTextAnswer.setTextColor(Color.RED)
                } else {
                    // Reset text color
                    editTextAnswer.setTextColor(Color.BLACK)
                }
            }
        })

        submit.setOnClickListener() {
            val intent = Intent(this, PowerBI::class.java)
            startActivity(intent)
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
