package com.example.pwd_app.viewModel.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pwd_app.viewModel.mapActivity.MainActivity2
import com.example.pwd_app.R
import com.example.pwd_app.Utilities.General
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.network.NetworkStatusUtility
import com.example.pwd_app.repository.DataRepository
import com.example.pwd_app.repository.LocalDatabaseRepository
import com.example.pwd_app.viewModel.localDbView.LocalDbView
import com.example.pwd_app.viewModel.localDbView.LocalDbViewModel
import com.example.pwd_app.viewModel.localDbView.LocalDbViewModelFactory
import com.example.pwd_app.viewModel.login.SessionManager
import com.example.pwd_app.viewModel.progress.WorkProgress
import com.example.pwd_app.viewModel.schoolDisplay.SchoolDisplay
import com.example.pwd_app.viewModel.upload.UploadViewModel
import com.example.pwd_app.viewModel.upload.UploadViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Profile : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var sessionManager: SessionManager
    private lateinit var localDbViewModel: LocalDbViewModel
    private lateinit var uploadViewModel: UploadViewModel
    private lateinit var atcOfficeText: TextView
    private lateinit var poOfficeText: TextView
    private lateinit var juniorEngineerNameText: TextView
    private lateinit var viewHistoryButton: Button
    private lateinit var logOutButton: Button
    private lateinit var workorderProgress: Button
    private lateinit var status: ImageView
    private lateinit var networkStatusUtility: NetworkStatusUtility
    private lateinit var viewLocalDBButton: Button
    private lateinit var uploadDbButton: Button
    private lateinit var localDbCount: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize views and perform other setup

        return inflater.inflate(R.layout.activity_profile, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(requireContext())
        val localDatabaseRepository = LocalDatabaseRepository(database)
        localDbViewModel = ViewModelProvider(
            this,
            LocalDbViewModelFactory(localDatabaseRepository)
        )[LocalDbViewModel::class.java]
        val dataRepository = DataRepository(apiInterface)
        uploadViewModel = ViewModelProvider(
            this,
            UploadViewModelFactory(dataRepository)
        )[UploadViewModel::class.java]

        // Initialize views
        atcOfficeText = view.findViewById(R.id.atc_office_text)
        poOfficeText = view.findViewById(R.id.po_office_text)
        juniorEngineerNameText = view.findViewById(R.id.junior_engineer_name_text)
        viewHistoryButton = view.findViewById(R.id.view_history_button)
        logOutButton = view.findViewById(R.id.logOutButton)
        workorderProgress = view.findViewById(R.id.progressButton)
        status = view.findViewById(R.id.statusIcon)
        viewLocalDBButton = view.findViewById(R.id.view_db_button)
        uploadDbButton = requireView().findViewById(R.id.upload_db_button)
        localDbCount = requireView().findViewById(R.id.local_dbCount)

        lifecycleScope.launch {
            localDbCount.text = "items in Local DB : " + database.Dao().getDbCount()
        }

        // Initialize networkStatusUtility
        networkStatusUtility = NetworkStatusUtility(requireContext())

        // Check network status and set button states
        if (networkStatusUtility.isNetworkAvailable) {
            status.setImageResource(R.drawable.online)
            uploadDbButton.alpha = 1.0f
            uploadDbButton.isEnabled = true
            viewHistoryButton.alpha = 1.0f
            viewHistoryButton.isEnabled = true
        } else {
            status.setImageResource(R.drawable.offline)
            uploadDbButton.alpha = 0.5f
            uploadDbButton.isEnabled = false
            viewHistoryButton.alpha = 0.5f
            viewHistoryButton.isEnabled = false
        }

        // Set network status listener
        networkStatusUtility.startMonitoringNetworkStatus(object :
            NetworkStatusUtility.NetworkStatusListener {
            override fun onNetworkAvailable() {
//                updateButtonStatus(true)
                status.setImageResource(R.drawable.online)
                uploadDbButton.alpha = 1.0f
                uploadDbButton.isEnabled = true
                viewHistoryButton.alpha = 1.0f
                viewHistoryButton.isEnabled = true
                status.setOnClickListener {
                    showToast("Online")
                }
            }

            override fun onNetworkLost() {
//                updateButtonStatus(false)
                status.setImageResource(R.drawable.offline)
                uploadDbButton.alpha = 0.5f
                uploadDbButton.isEnabled = false
                viewHistoryButton.alpha = 0.5f
                viewHistoryButton.isEnabled = false
                status.setOnClickListener {
                    showToast("Offline")
                }
            }
        })
        // Set initial values for TextViews
        atcOfficeText.text = Credentials.DEFAULT_ATC
        poOfficeText.text = Credentials.DEFAULT_PO
        juniorEngineerNameText.text = Credentials.DEFAULT_JUNIOR_ENGINEER


        viewHistoryButton.setOnClickListener {
            if (!networkStatusUtility.isNetworkAvailable) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Cannot Connect To the Server")
                    .setMessage("Please make Sure you have an Internet Connection to View History")
                    .setPositiveButton(
                        "OK"
                    ) { dialogInterface, _ -> dialogInterface.dismiss() }
            } else {
                // Show a loading dialog or progress bar here
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setMessage("Fetching data...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                // You can replace this delay with your actual data fetching and display logic
                Thread {
                    try {
                        Thread.sleep(1500) // Simulate data fetching taking 2 seconds
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    // Dismiss the loading dialog when data is fetched
                    requireActivity().runOnUiThread {
                        progressDialog.dismiss()

                        // Start the Fragment or Activity to display the data here
                        val displaySchoolFragment: Fragment = SchoolDisplay()
                        val fragmentManager = requireActivity().supportFragmentManager
                        General.replaceFragment(
                            fragmentManager,
                            R.id.container,
                            displaySchoolFragment,
                            true,
                            "DisplaySchoolFragmentTag",
                            R.anim.slide_in,  // Enter animation
                            R.anim.slide_out,  // Exit animation
                            0,  // Pop enter animation (you can specify one if needed)
                            0 // Pop exit animation (you can specify one if needed)
                        )
                    }
                }.start()
            }
        }

        uploadDbButton.setOnClickListener {
            localDbViewModel.surveyData.observe(viewLifecycleOwner) { imageData ->

                if (imageData.isNotEmpty()) { // Check if there is data to upload
                    try {
                        for (item in imageData) {
                            val schoolName = item.school_Name ?: ""
                            val poOffice = item.po_office ?: ""
                            val imageName = item.image_name ?: ""
                            val imageType = item.image_type ?: ""
                            val imagePdf = item.image_pdf ?: ""
                            val uploadDate = item.upload_date ?: ""
                            val uploadTime = item.upload_time ?: ""
                            val entryBy = item.EntryBy ?: ""
                            val latitude = item.Latitude ?: ""
                            val longitude = item.Longitude ?: ""
                            val userUploadDate = item.user_upload_date ?: ""
                            val inspectionType = item.InspectionType ?: ""
                            val workorderNumber = item.WorkorderNumber ?: ""
                            val description = item.Description ?: ""
                            val ags = item.ags ?: ""

                            // Call the uploadData function with extracted data
                            uploadViewModel.uploadData(
                                schoolName,
                                poOffice,
                                imageName,
                                imageType,
                                imagePdf,
                                uploadDate,
                                uploadTime,
                                entryBy,
                                latitude,
                                longitude,
                                userUploadDate,
                                inspectionType,
                                workorderNumber,
                                description,
                                ags
                            )
                            // Delete item from the database
                            lifecycleScope.launch {
                                database.Dao().deleteItems(schoolName, poOffice)
                                localDatabaseRepository.getCount()
                                localDbCount.text =
                                    "items in Local DB : " + database.Dao().getDbCount()
                            }
                        }
                        // Show a success AlertDialog
                        val successDialog = AlertDialog.Builder(requireContext())
                            .setTitle("Success")
                            .setMessage("Local Database Upload Successful")
                            .setPositiveButton("OK") { _, _ ->

                            }
                            .create()
                        successDialog.show()
                    } catch (e: Exception) {
                        // Show a failure AlertDialog
                        val failureDialog = AlertDialog.Builder(requireContext())
                            .setTitle("Failure")
                            .setMessage("Upload failed. Check your internet connection and try again.")
                            .setPositiveButton("OK") { _, _ ->

                            }
                            .create()
                        failureDialog.show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "There is no data in the local database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }



        viewLocalDBButton.setOnClickListener {
            lifecycleScope.launch {
                // Inside the coroutine scope
                localDatabaseRepository.getCount()
                val dbItemCount = database.Dao().getDbCount()

                if (dbItemCount > 0) {
                    val localdbFragment: Fragment = LocalDbView()
                    val fragmentManager = requireActivity().supportFragmentManager
                    General.replaceFragment(
                        fragmentManager,
                        R.id.container,
                        localdbFragment,
                        true,
                        "LocalDbFragmentTag",
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out, // Exit animation
                        0,                // Pop enter animation (you can specify one if needed)
                        0                 // Pop exit animation (you can specify one if needed)
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "There is No Data in the local database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        logOutButton.setOnClickListener {
            // Show a logout confirmation dialog
            showLogoutConfirmationDialog()
        }
        workorderProgress.setOnClickListener {
            val progressFragment: Fragment = WorkProgress()
            val fragmentManager = requireActivity().supportFragmentManager
            General.replaceFragment(
                fragmentManager,
                R.id.container,
                progressFragment,
                true,
                "progressFragmentTag",
                R.anim.slide_in,  // Enter animation
                R.anim.slide_out, // Exit animation
                0,                // Pop enter animation (you can specify one if needed)
                0                 // Pop exit animation (you can specify one if needed)
            )
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // Handle item selection
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Handle when nothing is selected
    }

    private fun showToast(statusText: String) {
        Toast.makeText(requireContext(), statusText, Toast.LENGTH_SHORT).show()
    }


    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                // User confirmed logout, show a progress dialog
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setMessage("Logging out...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                // Delay for 1 second (1000 milliseconds) and then perform the logout
                lifecycleScope.launch {
                    delay(1000)
                    progressDialog.dismiss() // Dismiss the progress dialog
                    performLogout() // Logout after the delay
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        // Clear the session data (if you're using sessions)
        sessionManager.logoutUser()

        // Navigate back to the login screen
        val intent = Intent(requireContext(), MainActivity2::class.java)
        startActivity(intent)
    }

}
