package com.example.pwd_app.viewModel.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.MainActivity
import com.example.pwd_app.R
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.repository.LoginRepository

class Login : AppCompatActivity(){
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loadingProgressBar = findViewById<ProgressBar>(R.id.loadingProgressBar)
        val loginApiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(applicationContext)
        val loginRepository = LoginRepository(loginApiInterface, database, applicationContext)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(loginRepository))[LoginViewModel::class.java]

        //Session Manager
        sessionManager = SessionManager(this)

        //selected variables
        var selectedAtcOffice = ""
        var selectedPoOffice = ""
        var selectedJe = ""
        var enteredPassword = ""

        // Find the views by their IDs
        val selectAtcOfficeSpinner = findViewById<Spinner>(R.id.select_atc_office)
        val selectPoOfficeSpinner = findViewById<Spinner>(R.id.select_po_office)
        val selectJuniorEngineerSpinner = findViewById<Spinner>(R.id.select_junior_engineer)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        // Check if the user is already logged in, and if so, redirect them to MainActivity
        if (sessionManager.isLoggedIn()) {
            val userDetails = sessionManager.getUserDetails()
            val atcValue = userDetails[SessionManager.KEY_ATC]
            val poValue = userDetails[SessionManager.KEY_PO]
            val juniorEngineerValue = userDetails[SessionManager.KEY_JE]
            redirectToMainActivity(atcValue, poValue, juniorEngineerValue)
        }

        //Observe the user and Populate the atc Spinner
        loginViewModel.users.observe(this) { userList ->
            val atcOffices = mutableListOf("Select ATC Office")
            atcOffices.addAll(userList.map { it.atc_office }.distinct())
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, atcOffices)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            selectAtcOfficeSpinner.adapter = adapter
        }

        selectAtcOfficeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedAtcOffice = selectAtcOfficeSpinner.selectedItem as String
                    //Observe the user and populate poSpinner
                    loginViewModel.users.observe(this@Login) { userList ->
                        val filteredPoOffice = mutableListOf("Select Po Office")
                        filteredPoOffice.addAll(userList
                            .filter { it.atc_office == selectedAtcOffice }
                            .map { it.po_office })
                        val adapter = ArrayAdapter(
                            this@Login,
                            android.R.layout.simple_spinner_item,
                            filteredPoOffice
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        selectPoOfficeSpinner.adapter = adapter
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        selectPoOfficeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedPoOffice = selectPoOfficeSpinner.selectedItem as String
                //Observe the user and populate poSpinner
                loginViewModel.users.observe(this@Login) { userList ->
                    val filteredJe = mutableListOf("Select JE")
                    filteredJe.addAll(userList
                        .filter { it.atc_office == selectedAtcOffice && it.po_office == selectedPoOffice && it.ErStatus=="Y"}
                        .map { it.username })
                    val adapter = ArrayAdapter(
                        this@Login,
                        android.R.layout.simple_spinner_item,
                        filteredJe
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    selectJuniorEngineerSpinner.adapter = adapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        selectJuniorEngineerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedJe = selectJuniorEngineerSpinner.selectedItem as String
                //Observe the user and populate poSpinner
                loginViewModel.users.observe(this@Login) { userList ->
                    val passKey = userList
                        .filter { it.atc_office == selectedAtcOffice && it.po_office == selectedPoOffice && it.username == selectedJe }
                        .map { it.password }

                    enteredPassword = passKey.toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        loginButton.setOnClickListener{

            loadingProgressBar.visibility = View.VISIBLE
            val inputPassword = passwordEditText.text.toString()
            Log.d("password", enteredPassword)
            if ( inputPassword=="" || selectedAtcOffice=="Select ATC Office" || selectedPoOffice=="Select Po Office"|| selectedJe=="Select JE") {
                Toast.makeText(
                    this@Login,
                    "Incorrect Password or Incorrect Credentials",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(inputPassword.equals(enteredPassword.substring(1, enteredPassword.length - 1))){
                onLoginSuccess(selectedAtcOffice, selectedPoOffice, selectedJe)

                Toast.makeText(this@Login, "Successful Login", Toast.LENGTH_SHORT).show()
                // Store user login details in the session
                sessionManager.createLoginSession(selectedAtcOffice, selectedPoOffice, selectedJe)
                val i = Intent(this@Login, MainActivity::class.java)
                startActivity(i)

                // Dismiss the progress dialog after starting MainActivity
                loadingProgressBar.visibility = View.GONE
            }else{
                Toast.makeText(
                    this@Login,
                    "Incorrect Password or Incorrect Credentials",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onLoginSuccess(atcValue: String, poValue: String, juniorEngineerValue: String) {
        Credentials.DEFAULT_ATC = atcValue
        Credentials.DEFAULT_PO = poValue
        Credentials.DEFAULT_JUNIOR_ENGINEER = juniorEngineerValue
    }

    private fun redirectToMainActivity(atcValue: String?, poValue: String?, juniorEngineerValue: String?) {
        if (atcValue != null && poValue != null && juniorEngineerValue != null) {
            Credentials.DEFAULT_ATC = atcValue
            Credentials.DEFAULT_PO = poValue
            Credentials.DEFAULT_JUNIOR_ENGINEER = juniorEngineerValue
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}