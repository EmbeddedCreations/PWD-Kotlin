package com.example.pwd_app.viewModel.edit

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.R
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.UploadObject
import com.example.pwd_app.network.NetworkStatusUtility
import com.example.pwd_app.repository.DataRepository
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.viewModel.home.HomeViewModel
import com.example.pwd_app.viewModel.home.HomeViewModelFactory
import com.example.pwd_app.viewModel.upload.UploadViewModel
import com.example.pwd_app.viewModel.upload.UploadViewModelFactory

class EditScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var uploadViewModel: UploadViewModel
    private var buttonSaveImage: Button? = null
    private var editTextDescription: EditText? = null
    private var progressDialog: ProgressDialog? = null
    private var networkStatusUtility: NetworkStatusUtility? = null
    private var iv_imgView: ImageView? = null
    private var status: ImageView? = null
    private var imageChanged = false
    private lateinit var spinnerBuilding: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(this)
        val dataRepository = DataRepository(apiInterface)
        uploadViewModel = ViewModelProvider(
            this,
            UploadViewModelFactory(dataRepository)
        ).get(UploadViewModel::class.java)
        val homeRepository = HomeRepository(apiInterface, database, this)
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(homeRepository))[HomeViewModel::class.java]
        val mainHandler = Handler(Looper.getMainLooper())
        setContentView(R.layout.edit_upload_details)
        status = findViewById(R.id.statusIcon)
        val textViewLoggedIn = findViewById<TextView>(R.id.textViewLoggedIn)
        editTextDescription = findViewById(R.id.editTextDescription)
        val juniorEngineer: String = Credentials.DEFAULT_JUNIOR_ENGINEER
        textViewLoggedIn.text = "Logged in as: $juniorEngineer"
        spinnerBuilding = findViewById(R.id.spinnerBuilding) // Replace with your actual spinner ID

        networkStatusUtility = NetworkStatusUtility(this)
        if (networkStatusUtility!!.isNetworkAvailable) {
            status?.setImageResource(R.drawable.online)
        } else {
            status?.setImageResource(R.drawable.offline)
        }
        // Capture a reference to networkStatusUtility in a local variable
        val utility = networkStatusUtility

        utility?.startMonitoringNetworkStatus(object : NetworkStatusUtility.NetworkStatusListener {
            override fun onNetworkAvailable() {
                mainHandler.post {
                    status?.setImageResource(R.drawable.online)
                    buttonSaveImage?.isEnabled = true
                    buttonSaveImage?.alpha = 1.0f
                    status?.setOnClickListener { showToast("Online") }
                }
            }

            override fun onNetworkLost() {
                mainHandler.post {
                    status?.setImageResource(R.drawable.offline)
                    buttonSaveImage?.isEnabled = false
                    buttonSaveImage?.alpha = 0.5f
                    status?.setOnClickListener { showToast("Offline") }
                }
            }
        })

        spinnerBuilding.onItemSelectedListener = this
        homeViewModel.buildings.observe(this) { buildingList ->
            val uniqueBuildings = mutableSetOf<String>() // Use a Set to ensure uniqueness

            // Add the default selected building
            uniqueBuildings.add(Credentials.SELECTED_BUILDING)

            // Filter and add unique building values from the buildingList
            buildingList
                .filter { it.unq_id == Credentials.SELECTED_SCHOOL_ID }
                .map { it.type_building.toString() }
                .forEach { uniqueBuildings.add(it) }

            // Convert the uniqueBuildings Set back to a List
            val buildings = uniqueBuildings.toList()
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                buildings
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerBuilding.adapter = adapter
        }
        buttonSaveImage?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!networkStatusUtility!!.isNetworkAvailable) {
                    Toast.makeText(
                        this@EditScreen,
                        "No internet connection available",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val description = editTextDescription?.text.toString().trim()
                if (description.isEmpty()) {
                    Toast.makeText(
                        this@EditScreen,
                        "Please enter a description.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (iv_imgView?.drawable == null) {
                    Toast.makeText(
                        this@EditScreen,
                        "Please select an image first.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!imageChanged) {
                    Toast.makeText(
                        this@EditScreen,
                        "Please select an image first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Initiase Values
                    progressDialog = ProgressDialog(this@EditScreen)
                    progressDialog?.setTitle("Uploading Image")
                    progressDialog?.setMessage("Please wait...")
                    progressDialog?.show()
                    buttonSaveImage!!.isEnabled = false

                    UploadObject.DESCRIPTION = description
                    UploadObject.IMAGE_NAME = Credentials.SELECTED_BUILDING
                    UploadObject.IMAGE_TYPE = "jpg"
                    uploadViewModel.uploadData(
                        UploadObject.SCHOOL_NAME,
                        UploadObject.PO_OFFICE,
                        UploadObject.IMAGE_NAME,
                        UploadObject.IMAGE_TYPE,
                        UploadObject.IMAGE_PDF,
                        UploadObject.UPLOAD_DATE,
                        UploadObject.UPLOAD_TIME,
                        UploadObject.ENTRYBY,
                        UploadObject.LATITUDE,
                        UploadObject.LONGITUDE,
                        UploadObject.USER_UPLOAD_DATE,
                        UploadObject.INSPECTIONTYPE,
                        UploadObject.WORKORDERNUMBER,
                        UploadObject.DESCRIPTION,
                        UploadObject.AGS
                    )
                    uploadViewModel.uploadStatus.observe(this@EditScreen) { isUploaded ->

                        progressDialog!!.dismiss()
                        // Re-enable the "Upload" button after the upload is completed
                        buttonSaveImage!!.isEnabled = true
                        editTextDescription?.setText("")

                        Toast.makeText(
                            this@EditScreen,
                            "Uploaded Successfull",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (isUploaded) {
                            // Dismiss the progress dialog
                            progressDialog!!.dismiss()
                            // Re-enable the "Upload" button after the upload is completed
                            buttonSaveImage!!.isEnabled = true

                            Toast.makeText(
                                this@EditScreen,
                                "Uploaded Successfull",
                                Toast.LENGTH_SHORT
                            ).show()
                            // redirect to the school list view history
                        } else {
                            progressDialog!!.dismiss()
                            // Re-enable the "Upload" button after the upload is completed
                            buttonSaveImage!!.isEnabled = true
                            Toast.makeText(
                                this@EditScreen,
                                "Uploaded Failed",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
            }
        })

    }

    private fun showToast(statusText: String) {
        Toast.makeText(this, statusText, Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinnerBuilding -> {
                val selectedItem = spinnerBuilding.selectedItem as? String
                Credentials.SELECTED_BUILDING = selectedItem ?: ""
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}