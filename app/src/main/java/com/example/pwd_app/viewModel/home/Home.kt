package com.example.pwd_app.viewModel.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.R
import com.example.pwd_app.Utilities.General
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.UploadObject
import com.example.pwd_app.network.NetworkStatusUtility
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.viewModel.upload.Upload
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Home : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var homeViewModel: HomeViewModel
    val uploadFragment: Fragment = Upload()

    // Declare the ProgressBar as a class-level property
    private lateinit var loadingProgressBar: ProgressBar

    //Selected variables
    var selectedSchool = "Select School"
    var selectedBuilding = "Select Building"
    var selectedWorkorder = "Select Workorder"
    var selectedId = ""
    var selectedWorkOrderNumber = "NA"

    //Buttons And TextViews
    private lateinit var spinnerSchool: Spinner
    private lateinit var spinnerBuilding: Spinner
    private lateinit var spinnerWorkorder: Spinner
    private lateinit var textViewSelectedDate: TextView
    private lateinit var buttonSurvey: Button
    private lateinit var textViewLoggedIn: TextView
    private lateinit var textViewAtc: TextView
    private lateinit var textViewPoOffice: TextView
    private val workorderNames =
        arrayOf("Select Workorder", "General Inspection", "Workorder related Inspection")
    private var networkStatusUtility: NetworkStatusUtility? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_home, container, false)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        // Initialize your UI components and set listeners here
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingProgressBar.visibility = View.VISIBLE
        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(requireContext())
        val homeRepository = HomeRepository(apiInterface, database, requireContext())
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(homeRepository))[HomeViewModel::class.java]

        // Initialize UI components here
        //Selected variables
        selectedSchool = "Select School"
        selectedBuilding = "Select Building"
        selectedWorkorder = "Select Workorder"
        selectedId = ""
        spinnerSchool = view.findViewById(R.id.spinnerSchool)
        spinnerBuilding = view.findViewById(R.id.spinnerBuilding)
        spinnerWorkorder = view.findViewById(R.id.spinnerWorkorder)
        textViewSelectedDate = view.findViewById(R.id.textViewSelectedDate)
        buttonSurvey = view.findViewById(R.id.buttonSurvey)
        textViewLoggedIn = view.findViewById(R.id.textViewLoggedIn)
        textViewAtc = view.findViewById(R.id.atc)
        textViewPoOffice = view.findViewById(R.id.po)

        //NetworkStatus
        val status = requireView().findViewById<ImageView>(R.id.statusIcon)
        networkStatusUtility = NetworkStatusUtility(requireContext())
        if (networkStatusUtility!!.isNetworkAvailable) {
            status.setImageResource(R.drawable.online)
        } else {
            status.setImageResource(R.drawable.offline)
        }
        networkStatusUtility!!.startMonitoringNetworkStatus(object :
            NetworkStatusUtility.NetworkStatusListener {
            override fun onNetworkAvailable() {
                status.setImageResource(R.drawable.online)
                status.setOnClickListener { showToast("Online") }
            }

            override fun onNetworkLost() {
                status.setImageResource(R.drawable.offline)
                status.setOnClickListener { showToast("Offline") }
            }
        })



        spinnerSchool.onItemSelectedListener = this
        spinnerBuilding.onItemSelectedListener = this
        spinnerWorkorder.onItemSelectedListener = this

        //Putting values into calendar
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        textViewSelectedDate.text = currentDate
        //into TexViews
        textViewLoggedIn.text = "Logged in as: " + Credentials.DEFAULT_JUNIOR_ENGINEER
        textViewAtc.text = "Atc Office: " + Credentials.DEFAULT_ATC
        textViewPoOffice.text = "PO Office: " + Credentials.DEFAULT_PO

        //School Spinner
        homeViewModel.schools.observe(viewLifecycleOwner) { schoolList ->
            val schools = mutableListOf("Select School")
            schools.addAll(schoolList.map { it.school_name.toString() })
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, schools)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSchool.adapter = adapter
        }

        val workorderAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, workorderNames
        )
        workorderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerWorkorder.adapter = workorderAdapter
        loadingProgressBar.visibility = View.GONE
        //Survey Button
        buttonSurvey.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                //var selectedDate = textViewSelectedDate.text.toString()
                if (selectedSchool == "Select School") {
                    showToast("Please select a school.")
                } else if (selectedBuilding == "Select Building") {
                    showToast("Please select a building.")
                } else if (selectedWorkorder == "Select Workorder") {
                    showToast("Please select a workorder.")
                } else {
                    //Set Data For Upload
                    UploadObject.SCHOOL_NAME = selectedId
                    UploadObject.PO_OFFICE = Credentials.DEFAULT_PO
                    UploadObject.USER_UPLOAD_DATE = currentDate
                    UploadObject.ENTRYBY = Credentials.DEFAULT_JUNIOR_ENGINEER
                    UploadObject.WORKORDERNUMBER = selectedWorkOrderNumber
                    UploadObject.INSPECTIONTYPE = selectedWorkorder
                    UploadObject.IMAGE_NAME = selectedBuilding
                    val fragmentManager = requireActivity().supportFragmentManager
                    General.replaceFragment(
                        fragmentManager,
                        R.id.container,
                        uploadFragment,
                        false,
                        "UploadFragmentTag",
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out,  // Exit animation
                        0,  // Pop enter animation (you can specify one if needed)
                        0 // Pop exit animation (you can specify one if needed)
                    )

                }
            }

            private fun showToast(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Handle item selection here
        when (parent?.id) {
            R.id.spinnerSchool -> {
                val selectedItem = spinnerSchool.selectedItem as? String
                selectedSchool = selectedItem ?: ""
                selectedId = (homeViewModel.schools.value?.get(position)?.id ?: "")
                homeViewModel.buildings.observe(viewLifecycleOwner) { buildingList ->
                    val buildings = mutableListOf("Select Building")
                    buildings.addAll(buildingList
                        .filter { it.unq_id == selectedId }
                        .map { it.type_building.toString() })
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        buildings
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerBuilding.adapter = adapter
                }
            }

            R.id.spinnerBuilding -> {
                val selectedItem = spinnerBuilding.selectedItem as? String
                selectedBuilding = selectedItem ?: ""
            }

            R.id.spinnerWorkorder -> {
                val selectedItem = spinnerWorkorder.selectedItem as? String
                selectedWorkorder = selectedItem ?: ""
                if (selectedWorkorder == "Workorder related Inspection") {
                    val textViewSecondDropdownTitle =
                        requireView().findViewById<TextView>(R.id.textViewSecondDropdownTitle)
                    val spinnerSecondDropdown =
                        requireView().findViewById<Spinner>(R.id.spinnerSecondDropdown)
                    textViewSecondDropdownTitle.visibility = View.VISIBLE
                    spinnerSecondDropdown.visibility = View.VISIBLE
                    homeViewModel.workOrders.observe(viewLifecycleOwner) { workOrderList ->
                        val workOrders = mutableListOf("Select Work Order")
                        workOrders.addAll(workOrderList
                            .filter { it.Unq_ID == selectedId }
                            .map { it.WorkorderNumber.toString() })
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            workOrders
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerSecondDropdown.adapter = adapter

                        spinnerSecondDropdown.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedWorkOrderNumber = parent.getItemAtPosition(position).toString()
                                    Credentials.SELECTED_WORKORDER_NUMBER = selectedWorkOrderNumber
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                    }
                } else {
                    val textViewSecondDropdownTitle =
                        requireView().findViewById<TextView>(R.id.textViewSecondDropdownTitle)
                    val spinnerSecondDropdown =
                        requireView().findViewById<Spinner>(R.id.spinnerSecondDropdown)
                    Credentials.SELECTED_WORKORDER_NUMBER =""
                    selectedWorkorder = "NA"
                    textViewSecondDropdownTitle.visibility = View.GONE
                    spinnerSecondDropdown.visibility = View.GONE
                }
            }
        }
    }

    private fun showToast(statusText: String) {
        Toast.makeText(requireContext(), statusText, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle nothing selected here
    }
}
