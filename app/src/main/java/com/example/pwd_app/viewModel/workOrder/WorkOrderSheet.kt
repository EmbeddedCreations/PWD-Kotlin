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
                    SelWeek1 = oddStates[work][0].toString(),
                    SelWeek2 = oddStates[work][1].toString(),
                    SelWeek3 = oddStates[work][2].toString(),
                    SelWeek4 = oddStates[work][3].toString(),
                    SelWeek5 = oddStates[work][4].toString(),
                    SelWeek6 = oddStates[work][5].toString(),
                    SelWeek7 = oddStates[work][6].toString(),
                    SelWeek8 = oddStates[work][7].toString(),
                    SelWeek9 = oddStates[work][8].toString(),
                    SelWeek10 = oddStates[work][9].toString(),
                    SelWeek11 = oddStates[work][10].toString(),
                    SelWeek12 = oddStates[work][11].toString(),
                    SelWeek13 = oddStates[work][12].toString(),
                    SelWeek14 = oddStates[work][13].toString(),
                    SelWeek15 = oddStates[work][14].toString(),
                    SelWeek16 = oddStates[work][15].toString(),
                    SelWeek17 = oddStates[work][16].toString(),
                    SelWeek18 = oddStates[work][17].toString(),
                    SelWeek19 = oddStates[work][18].toString(),
                    SelWeek20 = oddStates[work][19].toString(),
                    SelWeek21 = oddStates[work][20].toString(),
                    SelWeek22 = oddStates[work][21].toString(),
                    SelWeek23 = oddStates[work][22].toString(),
                    SelWeek24 = oddStates[work][23].toString(),
                    SelWeek25 = oddStates[work][24].toString(),
                    SelWeek26 = oddStates[work][25].toString(),
                    SelWeek27 = oddStates[work][26].toString(),
                    SelWeek28 = oddStates[work][27].toString(),
                    SelWeek29 = oddStates[work][28].toString(),
                    SelWeek30 = oddStates[work][29].toString(),
                    SelWeek31 = oddStates[work][30].toString(),
                    SelWeek32 = oddStates[work][31].toString(),
                    SelWeek33 = oddStates[work][32].toString(),
                    SelWeek34 = oddStates[work][33].toString(),
                    SelWeek35 = oddStates[work][34].toString(),
                    SelWeek36 = oddStates[work][35].toString(),
                    SelWeek37 = oddStates[work][36].toString(),
                    SelWeek38 = oddStates[work][37].toString(),
                    SelWeek39 = oddStates[work][38].toString(),
                    SelWeek40 = oddStates[work][39].toString(),
                    SelWeek41 = oddStates[work][40].toString(),
                    SelWeek42 = oddStates[work][41].toString(),
                    SelWeek43 = oddStates[work][42].toString(),
                    SelWeek44 = oddStates[work][43].toString(),
                    SelWeek45 = oddStates[work][44].toString(),
                    SelWeek46 = oddStates[work][45].toString(),
                    SelWeek47 = oddStates[work][46].toString(),
                    SelWeek48 = oddStates[work][47].toString(),
                    SelWeek49 = oddStates[work][48].toString(),
                    SelWeek50 = oddStates[work][49].toString(),
                    SelWeek51 = oddStates[work][50].toString(),
                    SelWeek52 = oddStates[work][51].toString(),
                    SelWeek53 = oddStates[work][52].toString(),
                    SelWeek54 = oddStates[work][53].toString(),
                    SelWeek55 = oddStates[work][54].toString(),
                    SelWeek56 = oddStates[work][55].toString(),
                    SelWeek57 = oddStates[work][56].toString(),
                    SelWeek58 = oddStates[work][57].toString(),
                    SelWeek59 = oddStates[work][58].toString(),
                    SelWeek60 = oddStates[work][59].toString(),
                    SelWeek61 = oddStates[work][60].toString(),
                    SelWeek62 = oddStates[work][61].toString(),
                    SelWeek63 = oddStates[work][62].toString(),
                    SelWeek64 = oddStates[work][63].toString(),
                    SelWeek65 = oddStates[work][64].toString(),
                    SelWeek66 = oddStates[work][65].toString(),
                    SelWeek67 = oddStates[work][66].toString(),
                    SelWeek68 = oddStates[work][67].toString(),
                    SelWeek69 = oddStates[work][68].toString(),
                    SelWeek70 = oddStates[work][69].toString(),
                    SelWeek71 = oddStates[work][70].toString(),
                    SelWeek72 = oddStates[work][71].toString(),
                    SelWeek73 = oddStates[work][72].toString(),
                    SelWeek74 = oddStates[work][73].toString(),
                    SelWeek75 = oddStates[work][74].toString(),
                    SelWeek76 = oddStates[work][75].toString(),
                    SelWeek77 = oddStates[work][76].toString(),
                    SelWeek78 = oddStates[work][77].toString(),
                    SelWeek79 = oddStates[work][78].toString(),
                    SelWeek80 = oddStates[work][79].toString(),
                    SelWeek81 = oddStates[work][80].toString(),
                    SelWeek82 = oddStates[work][81].toString(),
                    SelWeek83 = oddStates[work][82].toString(),
                    SelWeek84 = oddStates[work][83].toString(),
                    SelWeek85 = oddStates[work][84].toString(),
                    SelWeek86 = oddStates[work][85].toString(),
                    SelWeek87 = oddStates[work][86].toString(),
                    SelWeek88 = oddStates[work][87].toString(),
                    SelWeek89 = oddStates[work][88].toString(),
                    SelWeek90 = oddStates[work][89].toString(),
                    SelWeek91 = oddStates[work][90].toString(),
                    SelWeek92 = oddStates[work][91].toString(),
                    SelWeek93 = oddStates[work][92].toString(),
                    SelWeek94 = oddStates[work][93].toString(),
                    SelWeek95 = oddStates[work][94].toString(),
                    SelWeek96 = oddStates[work][95].toString()
                )
                workOrderViewModel.setWorkorderTimeline(
                    workOrderData.workorderNo,
                    workOrderData.itemOfWork,
                    workOrderData.countOfWeek,
                    workOrderData.SelWeek1,
                    workOrderData.SelWeek2,
                    workOrderData.SelWeek3,
                    workOrderData.SelWeek4,
                    workOrderData.SelWeek5,
                    workOrderData.SelWeek6,
                    workOrderData.SelWeek7,
                    workOrderData.SelWeek8,
                    workOrderData.SelWeek9,
                    workOrderData.SelWeek10,
                    workOrderData.SelWeek11,
                    workOrderData.SelWeek12,
                    workOrderData.SelWeek13,
                    workOrderData.SelWeek14,
                    workOrderData.SelWeek15,
                    workOrderData.SelWeek16,
                    workOrderData.SelWeek17,
                    workOrderData.SelWeek18,
                    workOrderData.SelWeek19,
                    workOrderData.SelWeek20,
                    workOrderData.SelWeek21,
                    workOrderData.SelWeek22,
                    workOrderData.SelWeek23,
                    workOrderData.SelWeek24,
                    workOrderData.SelWeek25,
                    workOrderData.SelWeek26,
                    workOrderData.SelWeek27,
                    workOrderData.SelWeek28,
                    workOrderData.SelWeek29,
                    workOrderData.SelWeek30,
                    workOrderData.SelWeek31,
                    workOrderData.SelWeek32,
                    workOrderData.SelWeek33,
                    workOrderData.SelWeek34,
                    workOrderData.SelWeek35,
                    workOrderData.SelWeek36,
                    workOrderData.SelWeek37,
                    workOrderData.SelWeek38,
                    workOrderData.SelWeek39,
                    workOrderData.SelWeek40,
                    workOrderData.SelWeek41,
                    workOrderData.SelWeek42,
                    workOrderData.SelWeek43,
                    workOrderData.SelWeek44,
                    workOrderData.SelWeek45,
                    workOrderData.SelWeek46,
                    workOrderData.SelWeek47,
                    workOrderData.SelWeek48,
                    workOrderData.SelWeek49,
                    workOrderData.SelWeek50,
                    workOrderData.SelWeek51,
                    workOrderData.SelWeek52,
                    workOrderData.SelWeek53,
                    workOrderData.SelWeek54,
                    workOrderData.SelWeek55,
                    workOrderData.SelWeek56,
                    workOrderData.SelWeek57,
                    workOrderData.SelWeek58,
                    workOrderData.SelWeek59,
                    workOrderData.SelWeek60,
                    workOrderData.SelWeek61,
                    workOrderData.SelWeek62,
                    workOrderData.SelWeek63,
                    workOrderData.SelWeek64,
                    workOrderData.SelWeek65,
                    workOrderData.SelWeek66,
                    workOrderData.SelWeek67,
                    workOrderData.SelWeek68,
                    workOrderData.SelWeek69,
                    workOrderData.SelWeek70,
                    workOrderData.SelWeek71,
                    workOrderData.SelWeek72,
                    workOrderData.SelWeek73,
                    workOrderData.SelWeek74,
                    workOrderData.SelWeek75,
                    workOrderData.SelWeek76,
                    workOrderData.SelWeek77,
                    workOrderData.SelWeek78,
                    workOrderData.SelWeek79,
                    workOrderData.SelWeek80,
                    workOrderData.SelWeek81,
                    workOrderData.SelWeek82,
                    workOrderData.SelWeek83,
                    workOrderData.SelWeek84,
                    workOrderData.SelWeek85,
                    workOrderData.SelWeek86,
                    workOrderData.SelWeek87,
                    workOrderData.SelWeek88,
                    workOrderData.SelWeek89,
                    workOrderData.SelWeek90,
                    workOrderData.SelWeek91,
                    workOrderData.SelWeek92,
                    workOrderData.SelWeek93,
                    workOrderData.SelWeek94,
                    workOrderData.SelWeek95,
                    workOrderData.SelWeek96
                )
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
