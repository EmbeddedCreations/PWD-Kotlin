package com.example.pwd_app

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.repository.LoginRepository
import com.example.pwd_app.viewModel.analytics.Analytics
import com.example.pwd_app.viewModel.home.Home
import com.example.pwd_app.viewModel.login.LoginViewModel
import com.example.pwd_app.viewModel.login.LoginViewModelFactory
import com.example.pwd_app.viewModel.profile.Profile
import com.example.pwd_app.viewModel.workOrder.WorkOrderSheet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private var homeFragment: Home = Home()
    private var profile: Profile = Profile()
    private var workOrderSheet: WorkOrderSheet = WorkOrderSheet()
    private var analytics: Analytics = Analytics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginApiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(applicationContext)
        val loginRepository = LoginRepository(loginApiInterface, database, applicationContext)
        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(loginRepository)
        )[LoginViewModel::class.java]
        supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    showConfirmationDialog {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, homeFragment).commit()
                    }
                    return@OnItemSelectedListener true
                }
                R.id.profile -> {
                    showConfirmationDialog {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        supportFragmentManager.beginTransaction().replace(R.id.container, profile)
                            .commit()
                    }
                    return@OnItemSelectedListener true
                }
                R.id.progress -> {
                    showConfirmationDialog {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, workOrderSheet).commit()
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                    return@OnItemSelectedListener true
                }
                R.id.analytics -> {
                    showConfirmationDialog {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        supportFragmentManager.beginTransaction().replace(R.id.container, analytics)
                            .commit()
                    }
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    private fun showConfirmationDialog(confirmationAction: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Unsaved Progress")
            .setMessage("Have you completed your work on this page? Progress may be lost.")
            .setPositiveButton("Continue") { dialogInterface, _ ->
                confirmationAction.invoke() // Invoke the provided action when user confirms
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                // Do nothing or add specific handling for cancel
            }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Exit") { dialogInterface, i ->
                super@MainActivity.onBackPressed()
            }
            .setNegativeButton("Cancel") { dialogInterface, i ->
                // Do nothing or add specific handling for cancel
            }
            .setCancelable(false)
            .show()
    }

}
