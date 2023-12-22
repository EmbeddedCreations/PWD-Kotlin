package com.example.pwd_app.viewModel.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.pwd_app.MainActivity2
import com.example.pwd_app.R
import com.example.pwd_app.viewModel.login.Login


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to full screen.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splashscreen)

        // Use a handler to run a task after a specific time interval.
        Handler().postDelayed({
            // Create a new intent.
            val i = Intent(this, MainActivity2::class.java)

            // Start a new activity.
            startActivity(i)

            // Finish the current activity.
            finish()
        }, 500)
    }
}