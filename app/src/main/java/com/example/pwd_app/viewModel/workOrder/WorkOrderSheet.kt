package com.example.pwd_app.viewModel.workOrder


import android.app.Dialog
import android.app.ProgressDialog
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
import android.widget.CompoundButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pwd_app.R
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.UploadTimelineModel
import com.example.pwd_app.repository.HomeRepository
import com.example.pwd_app.repository.TimeLineRepository
import com.example.pwd_app.repository.UploadTimelineRepository
import com.example.pwd_app.viewModel.home.HomeViewModel
import com.example.pwd_app.viewModel.home.HomeViewModelFactory

class WorkOrderSheet : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var workOrderDropdown: Spinner
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var tableLayout: TableLayout
    private lateinit var spinnerSchool: Spinner
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var uploadTimeline: Button
    lateinit var rowHeadings: Array<String>
    lateinit var weekCount: Array<String>
    private var selectedSchool = "Select School"
    private lateinit var editedWorks: MutableList<String>
    private lateinit var checkboxStates: Array<IntArray>
    private var selectedId = ""
    private lateinit var progressBar: ProgressBar
    private var loadingDialog: Dialog? = null
    private var counter = 0
    private var activeColumnIndex: Int = 0

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(requireContext())
        val homeRepository = HomeRepository(apiInterface, database, requireContext())
        val uploadTimelineRepository = UploadTimelineRepository(apiInterface)
        val timeLineRepository = TimeLineRepository(apiInterface, database, requireContext())

        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(homeRepository))[HomeViewModel::class.java]

        workOrderViewModel = ViewModelProvider(
            this,
            WorkOrderViewModelFactory(timeLineRepository, homeRepository,uploadTimelineRepository)
        )[WorkOrderViewModel::class.java]

        uploadTimeline = requireView().findViewById(R.id.saveWorkorder)
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

        uploadTimeline.setOnClickListener {
            // Increment the counter
            counter = activeColumnIndex
            counter++
            val oddStates = returnOddRowArrays(checkboxStates)
            var count = 0
            for(work in 0 until editedWorks.size){

                val workOrderData= UploadTimelineModel(
                    poOffice = Credentials.DEFAULT_PO,
                    workorderNo = Credentials.SELECTED_SCHOOL_FOR_WO,
                    countOfWeek = activeColumnIndex.toString(),
                    entryBy = Credentials.DEFAULT_JUNIOR_ENGINEER,
                    itemOfWork = editedWorks[work],
                    selWeek1 = oddStates[work][0].toString(),
                    selWeek2 = oddStates[work][1].toString(),
                    selWeek3 = oddStates[work][2].toString(),
                    selWeek4 = oddStates[work][3].toString(),
                    selWeek5 = oddStates[work][4].toString(),
                    selWeek6 = oddStates[work][5].toString(),
                    selWeek7 = oddStates[work][6].toString(),
                    selWeek8 = oddStates[work][7].toString(),
                    selWeek9 = oddStates[work][8].toString(),
                    selWeek10 = oddStates[work][9].toString(),
                    selWeek11 = oddStates[work][10].toString(),
                    selWeek12 = oddStates[work][11].toString(),
                    selWeek13 = oddStates[work][12].toString(),
                    selWeek14 = oddStates[work][13].toString(),
                    selWeek15 = oddStates[work][14].toString(),
                    selWeek16 = oddStates[work][15].toString(),
                    selWeek17 = oddStates[work][16].toString(),
                    selWeek18 = oddStates[work][17].toString(),
                    selWeek19 = oddStates[work][18].toString(),
                    selWeek20 = oddStates[work][19].toString(),
                    selWeek21 = oddStates[work][20].toString(),
                    selWeek22 = oddStates[work][21].toString(),
                    selWeek23 = oddStates[work][22].toString(),
                    selWeek24 = oddStates[work][23].toString(),
                    selWeek25 = oddStates[work][24].toString(),
                    selWeek26 = oddStates[work][25].toString(),
                    selWeek27 = oddStates[work][26].toString(),
                    selWeek28 = oddStates[work][27].toString(),
                    selWeek29 = oddStates[work][28].toString(),
                    selWeek30 = oddStates[work][29].toString(),
                    selWeek31 = oddStates[work][30].toString(),
                    selWeek32 = oddStates[work][31].toString(),
                    selWeek33 = oddStates[work][32].toString(),
                    selWeek34 = oddStates[work][33].toString(),
                    selWeek35 = oddStates[work][34].toString(),
                    selWeek36 = oddStates[work][35].toString(),
                    selWeek37 = oddStates[work][36].toString(),
                    selWeek38 = oddStates[work][37].toString(),
                    selWeek39 = oddStates[work][38].toString(),
                    selWeek40 = oddStates[work][39].toString(),
                    selWeek41 = oddStates[work][40].toString(),
                    selWeek42 = oddStates[work][41].toString(),
                    selWeek43 = oddStates[work][42].toString(),
                    selWeek44 = oddStates[work][43].toString(),
                    selWeek45 = oddStates[work][44].toString(),
                    selWeek46 = oddStates[work][45].toString(),
                    selWeek47 = oddStates[work][46].toString(),
                    selWeek48 = oddStates[work][47].toString(),
                    selWeek49 = oddStates[work][48].toString(),
                    selWeek50 = oddStates[work][49].toString(),
                    selWeek51 = oddStates[work][50].toString(),
                    selWeek52 = oddStates[work][51].toString(),
                    selWeek53 = oddStates[work][52].toString(),
                    selWeek54 = oddStates[work][53].toString(),
                    selWeek55 = oddStates[work][54].toString(),
                    selWeek56 = oddStates[work][55].toString(),
                    selWeek57 = oddStates[work][56].toString(),
                    selWeek58 = oddStates[work][57].toString(),
                    selWeek59 = oddStates[work][58].toString(),
                    selWeek60 = oddStates[work][59].toString(),
                    selWeek61 = oddStates[work][60].toString(),
                    selWeek62 = oddStates[work][61].toString(),
                    selWeek63 = oddStates[work][62].toString(),
                    selWeek64 = oddStates[work][63].toString(),
                    selWeek65 = oddStates[work][64].toString(),
                    selWeek66 = oddStates[work][65].toString(),
                    selWeek67 = oddStates[work][66].toString(),
                    selWeek68 = oddStates[work][67].toString(),
                    selWeek69 = oddStates[work][68].toString(),
                    selWeek70 = oddStates[work][69].toString(),
                    selWeek71 = oddStates[work][70].toString(),
                    selWeek72 = oddStates[work][71].toString(),
                    selWeek73 = oddStates[work][72].toString(),
                    selWeek74 = oddStates[work][73].toString(),
                    selWeek75 = oddStates[work][74].toString(),
                    selWeek76 = oddStates[work][75].toString(),
                    selWeek77 = oddStates[work][76].toString(),
                    selWeek78 = oddStates[work][77].toString(),
                    selWeek79 = oddStates[work][78].toString(),
                    selWeek80 = oddStates[work][79].toString(),
                    selWeek81 = oddStates[work][80].toString(),
                    selWeek82 = oddStates[work][81].toString(),
                    selWeek83 = oddStates[work][82].toString(),
                    selWeek84 = oddStates[work][83].toString(),
                    selWeek85 = oddStates[work][84].toString(),
                    selWeek86 = oddStates[work][85].toString(),
                    selWeek87 = oddStates[work][86].toString(),
                    selWeek88 = oddStates[work][87].toString(),
                    selWeek89 = oddStates[work][88].toString(),
                    selWeek90 = oddStates[work][89].toString(),
                    selWeek91 = oddStates[work][90].toString(),
                    selWeek92 = oddStates[work][91].toString(),
                    selWeek93 = oddStates[work][92].toString(),
                    selWeek94 = oddStates[work][93].toString(),
                    selWeek95 = oddStates[work][94].toString(),
                    selWeek96 = oddStates[work][95].toString()
                )
                workOrderViewModel.setWorkorderTimeline(workOrderData)
//                workOrderViewModel.uploadStatus.observe(viewLifecycleOwner){isUploaded->
//                    if (isUploaded) {
//                        // Reset UI elements on successful upload
//                        // Dismiss the progress dialog
//                        count++
//                    } else {
//
//                    }
//                }
//                if(count == editedWorks.size){
//                    progressDialog!!.dismiss()
//                }
            }

            Log.d("Odd Entries",editedWorks.toString())
            // Determine the active column index based on the counter
            activeColumnIndex = counter % 97

            // Disable columns based on the activeColumnIndex
            disableColumns(activeColumnIndex)


            // Show a message when the maximum limit is reached and reset the counter
            if (counter > 96) {
                counter = 0
                Toast.makeText(
                    requireContext(),
                    "Maximum limit of 96 columns reached. Columns have been reset.",
                    Toast.LENGTH_SHORT
                ).show()
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
                        Credentials.SELECTED_WORKORDER_NUMBER = parent.getItemAtPosition(position).toString()
                        val work = timeLines
                            .filter { it.workorder_no == Credentials.SELECTED_WORKORDER_NUMBER }
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

                        fun generateWeekCount():Array<String>{
                            return timeLines
                                .filter { it.workorder_no == Credentials.SELECTED_WORKORDER_NUMBER }
                                .flatMap {
                                    listOfNotNull(
                                        it.countofweek
                                    )
                                }
                                .toTypedArray()
                        }

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

                        rowHeadings = generateRowHeadings()
                        weekCount = generateWeekCount()
                        if(weekCount.isNotEmpty()){
                            activeColumnIndex = weekCount[0].toInt()+1
                        }

                        editedWorks =  mutableListOf<String>()
                        for(i in 1 until rowHeadings.size step  2){
                            val row = rowHeadings[i]
                            editedWorks.add(row)
                        }
                        Log.d("Edited Works", editedWorks.toString())
                        val numRows = rowHeadings.size
                        Log.d("CheckSheet->numRows", numRows.toString())

                        // Generate checkbox states based on values from the 'work' array
                        checkboxStates = Array(numRows) { rowIndex ->
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

        val view = inflater.inflate(R.layout.workorder_checksheet, container, false)

        tableLayout = view.findViewById(R.id.tableLayout)
        return view
    }

    private fun disableColumns(activeIndex: Int) {
        val tableLayout = requireView().findViewById<TableLayout>(R.id.tableLayout)

        // Disable all columns except the one with activeIndex
        for (rowIndex in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(rowIndex) as TableRow
            for (columnIndex in 1 until row.childCount) {
                val child = row.getChildAt(columnIndex)
                if (child is CheckBox) {
                    // Disable all checkboxes except the one with activeIndex
                    child.isEnabled = columnIndex == activeIndex
                }
            }
            // Disable odd rows
            if (rowIndex % 2 != 0) {
                for (columnIndex in 1 until row.childCount) {
                    val child = row.getChildAt(columnIndex)
                    if (child is CheckBox) {
                        child.isEnabled = false
                    }
                }
            }
        }
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
            currentProgressRow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightGreen))
        } else {
            currentProgressRow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightRed))
        }
    }

    private fun countCheckedCheckboxes(row: TableRow): Int {
        var checkedCount = 0
        for (i in 1 until row.childCount) {
            val checkBox = row.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
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

    private fun returnOddRowArrays(checkboxStates: Array<IntArray>): List<IntArray> {
        val oddRowArrays = mutableListOf<IntArray>()


        for (i in 1 until checkboxStates.size step 2) {
            val oddRowArray = checkboxStates[i]
            oddRowArrays.add(oddRowArray)
        }
        return oddRowArrays
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
                        val columnIndex = j // Make 'j' effectively final
                        val checkBox = CheckBox(requireContext())

                        // Check if the checkbox should be checked based on the checkboxStates array
                        checkBox.isChecked = checkboxStates[i - 1][checkboxIndex] == 1

                        // Add an OnCheckedChangeListener to prevent unchecking and update the array
                        checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                            if (!isChecked) {
                                // If the checkbox is unchecked, force it to be checked
                                checkBox.isChecked = true
                                Toast.makeText(
                                    requireContext(),
                                    "Checkbox is now checked and cannot be unchecked.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@OnCheckedChangeListener
                            }

                            // Update the corresponding value in the checkboxStates array
                            val rowIndex = tableLayout.indexOfChild(row)
                            val checkboxArrayIndex = (columnIndex - 1) * 4 + k
                            checkboxStates[rowIndex - 1][checkboxArrayIndex] =
                                if (isChecked) 1 else 0

                            // Print the updated array to the console
                            for (i in 0 until numRows) {
                                for (j in 0 until numCols * 4) {
                                    print(checkboxStates[i][j].toString() + " ")
                                }
                                println() // Move to the next row
                            }
                            // Call the function to compare and set the background color for the current row
                            compareAndSetColor(rowIndex)
                        })
                        // Set checkboxes in even rows to be disabled
                        if ((i - 1) % 2 == 0) {
                            checkBox.isEnabled = false
                        }
                        row.addView(checkBox)
                    }
                }
            }
            tableLayout.addView(row)
        }
        uploadTimeline.visibility = View.VISIBLE
        disableColumns(activeColumnIndex)
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.SelectedSchool -> {
                tableLayout.removeAllViews()
                uploadTimeline.visibility = View.INVISIBLE
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

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
