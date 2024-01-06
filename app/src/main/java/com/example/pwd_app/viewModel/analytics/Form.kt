package com.example.pwd_app.viewModel.analytics

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.R
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.WorkLog
import com.example.pwd_app.repository.UploadWorkLogRepository
import com.example.pwd_app.viewModel.home.Home
import com.example.pwd_app.viewModel.profile.Profile


class Form : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var textViewRatingFeedback: TextView
    private lateinit var editTextAnswer: EditText
    private lateinit var submit: Button
    private lateinit var workCardViewModel : WorkCardViewModel
    private lateinit var siteVisited : RadioGroup
    private lateinit var uploadPhotos : RadioGroup
    private lateinit var payment : EditText
    private lateinit var complete : CheckBox
    private lateinit var onTime : RadioGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        ratingBar = findViewById(R.id.ratingBar)
        textViewRatingFeedback = findViewById(R.id.textViewRatingFeedback)
        submit = findViewById(R.id.buttonSubmit)
        siteVisited = findViewById(R.id.radioGroup1)
        uploadPhotos = findViewById(R.id.radioGroup2)
        payment = findViewById(R.id.editTextAnswer)
        complete = findViewById(R.id.checkBoxAgree)
        onTime = findViewById(R.id.radioGroup3)

        val radioYes1: RadioButton = findViewById(R.id.radioYes1)
        val radioNo1: RadioButton = findViewById(R.id.radioNo1)

        val radioYes2: RadioButton = findViewById(R.id.radioYes2)
        val radioNo2: RadioButton = findViewById(R.id.radioNo2)

        val radioYes3: RadioButton = findViewById(R.id.radioYes3)
        val radioNo3: RadioButton = findViewById(R.id.radioNo3)
        siteVisited.setOnCheckedChangeListener{group ,checkedId ->
            when (checkedId) {
                R.id.radioYes1 -> {
                    WorkLog.sitePhysicalVisit = radioYes1.text.toString()
                    Log.d("WorkLog", WorkLog.sitePhysicalVisit)
                }
                R.id.radioNo1 -> {
                    WorkLog.sitePhysicalVisit = radioNo1.text.toString()
                    Log.d("WorkLog", WorkLog.sitePhysicalVisit)
                }
            }
        }
        uploadPhotos.setOnCheckedChangeListener{group,checkedId ->
            when (checkedId) {
                R.id.radioYes2 -> {
                    WorkLog.isPhotoUploaded = radioYes2.text.toString()
                    Log.d("WorkLog", WorkLog.isPhotoUploaded)
                }
                R.id.radioNo2 -> {
                    WorkLog.isPhotoUploaded = radioNo2.text.toString()
                    Log.d("WorkLog", WorkLog.isPhotoUploaded)
                }
            }
        }
        onTime.setOnCheckedChangeListener{group,checkedId ->
            when (checkedId) {
                R.id.radioYes3 -> {
                    WorkLog.isWorkOnTime = radioYes3.text.toString()
                    Log.d("WorkLog", WorkLog.isWorkOnTime)
                }
                R.id.radioNo3 -> {
                    WorkLog.isWorkOnTime  = radioNo3.text.toString()
                    Log.d("WorkLog", WorkLog.isWorkOnTime )
                }
            }
        }
        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val uploadWorkLogRepository = UploadWorkLogRepository(apiInterface)
        workCardViewModel = ViewModelProvider(
            this,
            WorkCardViewModelFactory(apiInterface,uploadWorkLogRepository)
        )[WorkCardViewModel::class.java]

        // Set a listener to the RatingBar
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            updateRatingFeedback(rating)
        }

        // Set up TextWatcher for EditText
        payment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                // Check if the entered text is not a number
                if (!editable.toString().matches(Regex("\\d*"))) {
                    // Show warning or change text color to red
                    payment.setTextColor(Color.RED)
                } else {
                    // Reset text color
                    payment.setTextColor(Color.BLACK)
                }
            }
        })

        submit.setOnClickListener() {

            val intent = Intent(this, Workorder::class.java)

            val inputText = payment.text.toString()
            WorkLog.amountReleased = if (inputText.isNotEmpty()) ({
                inputText.toInt()
            }).toString() else ({
                0
            }).toString()
            val result = if (complete.isChecked) {
                "Complete"
            } else {
                "Incomplete"
            }
            workCardViewModel.updateLog(
                WorkLog.ID,
                WorkLog.assignedJE,
                WorkLog.weekNumber,
                WorkLog.isWorkOnTime,
                WorkLog.isPhotoUploaded,
                WorkLog.sitePhysicalVisit,
                WorkLog.amountReleased,
                WorkLog.progressRating,
                result
            )
            workCardViewModel.editStatus.observe(this) { editStatus ->
                
                if (editStatus) {
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Update Failed Please Try Again", Toast.LENGTH_SHORT).show()
                }
            }

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
        WorkLog.progressRating = rating.toString()
    }
}
