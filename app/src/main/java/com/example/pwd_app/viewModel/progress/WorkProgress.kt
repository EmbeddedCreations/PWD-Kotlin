package com.example.pwd_app.viewModel.progress

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.R
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.repository.TimeLineRepository
import com.example.pwd_app.repository.UploadTimelineRepository
import com.example.pwd_app.viewModel.home.HomeViewModel
import com.example.pwd_app.viewModel.home.HomeViewModelFactory
import com.example.pwd_app.viewModel.workOrder.WorkOrderViewModel
import com.example.pwd_app.viewModel.workOrder.WorkOrderViewModelFactory

class WorkProgress : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var workOrderDropdown: Spinner
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var tableLayout: TableLayout
    private lateinit var spinnerSchool: Spinner
    private lateinit var saveWorkorder: Button
    private lateinit var homeViewModel: HomeViewModel
    private var selectedSchool = "Select School"
    private var selectedId = ""
    private lateinit var progressBar: ProgressBar
    private var loadingDialog: Dialog? = null

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(requireContext())
        val homeRepository = HomeRepository(apiInterface, database, requireContext())
        val timeLineRepository = TimeLineRepository(apiInterface, database, requireContext())
        val uploadTimelineRepository = UploadTimelineRepository(apiInterface)
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(homeRepository))[HomeViewModel::class.java]

        workOrderViewModel = ViewModelProvider(
            this,
            WorkOrderViewModelFactory(timeLineRepository, homeRepository, uploadTimelineRepository)
        )[WorkOrderViewModel::class.java]


        workOrderDropdown = requireView().findViewById(R.id.workOrder)
        spinnerSchool = requireView().findViewById(R.id.SelectedSchool)
        progressBar = requireView().findViewById(R.id.progressbar)
        // Observe the loading state from ViewModel
        workOrderViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }

        spinnerSchool.onItemSelectedListener = this
        homeViewModel.schools.observe(viewLifecycleOwner) { schoolList ->
            val schools = schoolList.map { it.school_name.toString() }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, schools)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSchool.adapter = adapter
        }
        workOrderViewModel.timeLine.observe(viewLifecycleOwner) { timeLines ->

            val workOrders = mutableListOf("Select Work Order")
            workOrders.addAll(timeLines
                .filter {
                    it.school_name.toString().trim() == Credentials.SELECTED_SCHOOL_FOR_WO.trim()
                }
                .map { it.workorder_no.toString() }
                .distinct())


            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, workOrders)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            workOrderDropdown.adapter = adapter

            val workOrderIndex = workOrders.indexOf(Credentials.SELECTED_WORKORDER_NUMBER)
            if (workOrderIndex != -1) {
                workOrderDropdown.setSelection(workOrderIndex)
            }
            workOrderDropdown.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        Credentials.SELECTED_WORKORDER_NUMBER =
                            parent.getItemAtPosition(position).toString()

                        val work = timeLines
                            .filter {
                                it.school_name.toString().trim() == Credentials.SELECTED_SCHOOL_FOR_WO.trim()
                            }
                            .flatMap { timeline ->
                                listOf(
                                    timeline.SelWeek1?.toInt(),
                                    timeline.SelWeek2?.toInt(),
                                    timeline.SelWeek3?.toInt(),
                                    timeline.SelWeek4?.toInt(),
                                    timeline.SelWeek5?.toInt(),
                                    timeline.SelWeek6?.toInt(),
                                    timeline.SelWeek7?.toInt(),
                                    timeline.SelWeek8?.toInt(),
                                    timeline.SelWeek9?.toInt(),
                                    timeline.SelWeek10?.toInt(),
                                    timeline.SelWeek11?.toInt(),
                                    timeline.SelWeek12?.toInt(),
                                    timeline.SelWeek13?.toInt(),
                                    timeline.SelWeek14?.toInt(),
                                    timeline.SelWeek15?.toInt(),
                                    timeline.SelWeek16?.toInt(),
                                    timeline.SelWeek17?.toInt(),
                                    timeline.SelWeek18?.toInt(),
                                    timeline.SelWeek19?.toInt(),
                                    timeline.SelWeek20?.toInt(),
                                    timeline.SelWeek21?.toInt(),
                                    timeline.SelWeek22?.toInt(),
                                    timeline.SelWeek23?.toInt(),
                                    timeline.SelWeek24?.toInt(),
                                    timeline.SelWeek25?.toInt(),
                                    timeline.SelWeek26?.toInt(),
                                    timeline.SelWeek27?.toInt(),
                                    timeline.SelWeek28?.toInt(),
                                    timeline.SelWeek29?.toInt(),
                                    timeline.SelWeek30?.toInt(),
                                    timeline.SelWeek31?.toInt(),
                                    timeline.SelWeek32?.toInt(),
                                    timeline.SelWeek33?.toInt(),
                                    timeline.SelWeek34?.toInt(),
                                    timeline.SelWeek35?.toInt(),
                                    timeline.SelWeek36?.toInt(),
                                    timeline.SelWeek37?.toInt(),
                                    timeline.SelWeek38?.toInt(),
                                    timeline.SelWeek39?.toInt(),
                                    timeline.SelWeek40?.toInt(),
                                    timeline.SelWeek41?.toInt(),
                                    timeline.SelWeek42?.toInt(),
                                    timeline.SelWeek43?.toInt(),
                                    timeline.SelWeek44?.toInt(),
                                    timeline.SelWeek45?.toInt(),
                                    timeline.SelWeek46?.toInt(),
                                    timeline.SelWeek47?.toInt(),
                                    timeline.SelWeek48?.toInt(),
                                    timeline.SelWeek49?.toInt(),
                                    timeline.SelWeek50?.toInt(),
                                    timeline.SelWeek51?.toInt(),
                                    timeline.SelWeek52?.toInt(),
                                    timeline.SelWeek53?.toInt(),
                                    timeline.SelWeek54?.toInt(),
                                    timeline.SelWeek55?.toInt(),
                                    timeline.SelWeek56?.toInt(),
                                    timeline.SelWeek57?.toInt(),
                                    timeline.SelWeek58?.toInt(),
                                    timeline.SelWeek59?.toInt(),
                                    timeline.SelWeek60?.toInt(),
                                    timeline.SelWeek61?.toInt(),
                                    timeline.SelWeek62?.toInt(),
                                    timeline.SelWeek63?.toInt(),
                                    timeline.SelWeek64?.toInt(),
                                    timeline.SelWeek65?.toInt(),
                                    timeline.SelWeek66?.toInt(),
                                    timeline.SelWeek67?.toInt(),
                                    timeline.SelWeek68?.toInt(),
                                    timeline.SelWeek69?.toInt(),
                                    timeline.SelWeek70?.toInt(),
                                    timeline.SelWeek71?.toInt(),
                                    timeline.SelWeek72?.toInt(),
                                    timeline.SelWeek73?.toInt(),
                                    timeline.SelWeek74?.toInt(),
                                    timeline.SelWeek75?.toInt(),
                                    timeline.SelWeek76?.toInt(),
                                    timeline.SelWeek77?.toInt(),
                                    timeline.SelWeek78?.toInt(),
                                    timeline.SelWeek79?.toInt(),
                                    timeline.SelWeek80?.toInt(),
                                    timeline.SelWeek81?.toInt(),
                                    timeline.SelWeek82?.toInt(),
                                    timeline.SelWeek83?.toInt(),
                                    timeline.SelWeek84?.toInt(),
                                    timeline.SelWeek85?.toInt(),
                                    timeline.SelWeek86?.toInt(),
                                    timeline.SelWeek87?.toInt(),
                                    timeline.SelWeek88?.toInt(),
                                    timeline.SelWeek89?.toInt(),
                                    timeline.SelWeek90?.toInt(),
                                    timeline.SelWeek91?.toInt(),
                                    timeline.SelWeek92?.toInt(),
                                    timeline.SelWeek93?.toInt(),
                                    timeline.SelWeek94?.toInt(),
                                    timeline.SelWeek95?.toInt(),
                                    timeline.SelWeek96?.toInt()
                                )
                            }
                            .toTypedArray()
                        // Calculate the number of rows needed
                        Log.d("CheckSheet->Work", work.toList().toString())

                        fun generateRowHeadings(): Array<String> {
                            return timeLines
                                .filter { it.workorder_no == Credentials.SELECTED_WORKORDER_NUMBER }
                                .flatMap {
                                    listOfNotNull(
                                        it.itemofwork
                                    )
                                }
                                .toTypedArray()
                        }

                        val rowHeadings = generateRowHeadings()
                        Log.d("CheckSheet->rowHeadings", rowHeadings.toList().toString())
                        val numRows = rowHeadings.size
                        Log.d("CheckSheet->numRows", numRows.toString())

                        // Generate checkbox states based on values from the 'work' array
                        val checkboxStates = Array(numRows) { rowIndex ->
                            IntArray(24 * 4) { columnIndex ->
                                // Use the corresponding value from the 'work' array if it exists, otherwise use 0
                                work.getOrNull(rowIndex * (24 * 4) + columnIndex) ?: 0
                            }
                        }
                        val columnHeadings = Array(24) { index -> "Month ${index + 1}" }
                        Log.d("CheckSheet->columnHeadings", columnHeadings.toList().toString())
                        val numCols = columnHeadings.size
                        if (numRows > 0 && numCols > 0 && rowHeadings.isNotEmpty()) {
                            createDynamicTable(
                                numRows,
                                numCols,
                                columnHeadings,
                                rowHeadings,
                                checkboxStates
                            )
                            // Call setRowColors after creating the table
                            setRowColors()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_work_progress, container, false)

        tableLayout = view.findViewById(R.id.tableLayout)
        return view
    }

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = Dialog(requireContext())
            loadingDialog?.setContentView(R.layout.progress_dialog)
            loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loadingDialog?.setCancelable(false)
        }
        loadingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun compareAndSetColor(currentRowIndex: Int) {
        val tableLayout = requireView().findViewById<TableLayout>(R.id.tableLayout)
        val currentProgressRow = tableLayout.getChildAt(currentRowIndex) as TableRow
        val previousProgressRow = tableLayout.getChildAt(currentRowIndex - 1) as TableRow
        var allMatch = true

        for (j in 1 until currentProgressRow.childCount) {
            if (
                countCheckedCheckboxes(previousProgressRow) < countCheckedCheckboxes(
                    currentProgressRow
                ) ||
                getLastCheckedCheckboxIndex(currentProgressRow) > getLastCheckedCheckboxIndex(
                    previousProgressRow
                )
            ) {
                allMatch = false
                break
            }
        }

        if (allMatch) {
            if(countCheckedCheckboxes(currentProgressRow)==0){
                currentProgressRow.setBackgroundColor(Color.TRANSPARENT);

            }else{
                currentProgressRow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightGreen))
            }
        } else {
            currentProgressRow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightRed))
        }
    }

    private fun countCheckedCheckboxes(row: TableRow): Int {
        var checkedCount = 0
        for (i in 1 until row.childCount) {
            val view = row.getChildAt(i)
            if (view is CheckBox && view.isChecked) {
                checkedCount++
            }
        }
        return checkedCount
    }


    private fun getLastCheckedCheckboxIndex(row: TableRow): Int {
        for (i in row.childCount - 1 downTo 1) {
            val checkBox = row.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                return i
            }
        }
        return -1 // Return -1 if no checkbox is checked in the row
    }

    private fun createDynamicTable(
        numRows: Int,
        numCols: Int,
        colLabels: Array<String>,
        rowLabels: Array<String>,
        checkboxStates: Array<IntArray>
    ) {
        // Check if the tableLayout already has child views (i.e., if the table exists)
        if (tableLayout.childCount > 0) {
            // If the table already exists, clear it by removing all child views
            tableLayout.removeAllViews()
        }
        // Create header row with column labels and checkboxes
        val headerRow = TableRow(requireContext())
        for (j in 0..numCols) {
            val textView = TextView(requireContext())
            textView.text = if (j == 0) "" else colLabels[j - 1]
            headerRow.addView(textView)

            // Add three more checkboxes under each column in the header
            for (k in 0..2) {
                val checkBox = CheckBox(requireContext())
                checkBox.visibility = View.INVISIBLE
                headerRow.addView(checkBox)
            }
        }
        tableLayout.addView(headerRow)

        // Create rows with labels and checkboxes
        for (i in 1..numRows) {
            val row = TableRow(requireContext())
            for (j in 0..numCols) {
                if (j == 0) {
                    // Add row label (without checkbox)
                    val textView = TextView(requireContext())
                    textView.text = rowLabels[i - 1]
                    row.addView(textView)
                } else {
                    // Add four checkboxes in the data cells (including the new row)
                    for (k in 0..3) {
                        val checkboxIndex = (j - 1) * 4 + k // Make 'j' effectively final
                        val checkBox = CheckBox(requireContext())

                        // Check if the checkbox should be checked based on the checkboxStates array
                        checkBox.isChecked = checkboxStates[i - 1][checkboxIndex] == 1
                        checkBox.isEnabled = false
                        row.addView(checkBox)
                    }
                }
            }
            tableLayout.addView(row)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.SelectedSchool -> {
                tableLayout.removeAllViews()
                val selectedItem = spinnerSchool.selectedItem as? String
                Credentials.SELECTED_SCHOOL_FOR_WO = selectedItem.toString()
                selectedSchool = selectedItem ?: ""
                selectedId = (homeViewModel.schools.value?.get(position)?.id ?: "")
                workOrderViewModel.fetchTimeline()
                Credentials.SELECTED_SCHOOL_FOR_WO = selectedSchool
                Log.d("selected School", Credentials.SELECTED_SCHOOL_FOR_WO)
            }
        }
    }

    // Add this function to set the colors after the table is created
    private fun setRowColors() {
        val numRows = tableLayout.childCount

        for (rowIndex in 2 until numRows step 2) {
            // Call compareAndSetColor function for odd rows (1-based index)
            compareAndSetColor(rowIndex)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
