package com.example.pwd_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.repository.LoginRepository
import com.example.pwd_app.viewModel.home.Home
import com.example.pwd_app.viewModel.login.LoginViewModel
import com.example.pwd_app.viewModel.login.LoginViewModelFactory
import com.example.pwd_app.viewModel.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var bottomNavigationView: BottomNavigationView

    var homeFragment: Home = Home()
    var profileFragment: ProfileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginApiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(applicationContext)
        val loginRepository = LoginRepository(loginApiInterface,database,applicationContext)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(loginRepository)).get(
            LoginViewModel::class.java)

        supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment)
                        .commit()
                    return@OnItemSelectedListener true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, profileFragment).commit()
                    return@OnItemSelectedListener true
                }
//
//                R.id.progress -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.container, workOrderCheckSheet).commit()
//                    return@OnItemSelectedListener true
//                }
//
//                R.id.analytics -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.container, AnalyticsFragment).commit()
//                    return@OnItemSelectedListener true
//                }
            }
            false
        })
    }
}