package com.example.pwd_app.viewModel.workOrder


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pwd_app.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkOrderSheet : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var checkboxStates: Array<IntArray>
    private lateinit var plannedDate: Date
    private lateinit var systemDate: Date
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
            currentProgressRow.setBackgroundColor(Color.TRANSPARENT)
        } else {
            currentProgressRow.setBackgroundColor(Color.RED)
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


    @OptIn(DelicateCoroutinesApi::class)
    private fun enableColumnsBasedOnDate() {
        val currentDate = Date() // Get the current system date
        val differenceInDays = calculateDateDifference(plannedDate, currentDate)
        println("Difference in days: $differenceInDays")

        val tableLayout = requireView().findViewById<TableLayout>(R.id.tableLayout)
        val headerRow = tableLayout.getChildAt(0) as TableRow

        // Determine the number of columns to enable based on the date difference
        val columnsToEnable = when {
            differenceInDays <= 7 -> 1
            differenceInDays <= 45 -> 15
            else -> 0 // Disable all columns by default
        }

        GlobalScope.launch(Dispatchers.Default) {
            // Loop through each column and enable or disable checkboxes in rows as needed
            for (j in 1 until headerRow.childCount) { // Start from 1 to skip the first column
                for (i in 1 until tableLayout.childCount) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val columnView = row.getChildAt(j)
                    if (columnView is CheckBox) {
                        // Enable checkboxes only for odd rows in this column
                        if (i % 2 == 0) {
                            columnView.isEnabled = j <= columnsToEnable
                        } else {
                            // Disable checkboxes in even rows for the same column
                            columnView.isEnabled = false
                        }
                    }
                }
            }
            // Update UI on the main thread
            withContext(Dispatchers.Main) {
                // Notify the UI thread that the task is complete
                // You can perform any UI updates or additional tasks here
            }
        }
    }

    private fun calculateDateDifference(startDate: Date, endDate: Date): Long {
        val difference = endDate.time - startDate.time
        return difference / (24 * 60 * 60 * 1000) // Convert milliseconds to days
    }

    private fun parseDate(format: String, dateStr: String): Date {
        val sdf = SimpleDateFormat(format, Locale.US)
        return sdf.parse(dateStr) ?: Date()
    }

    private fun createDynamicTable(
        view: View,
        numRows: Int,
        numCols: Int,
        colLabels: Array<String>,
        rowLabels: Array<String>,
        checkboxStates: Array<IntArray>
    ) {
        val tableLayout = view.findViewById<TableLayout>(R.id.tableLayout)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.workorder_checksheet, container, false)

        // Example values, replace with your actual input values
        val columnHeadings = arrayOf("Month1", "Month 2", "Month 3", "Month 4")
        val rowHeadings = arrayOf(
            "work 1 schedule",
            "work 1 progress",
            "work 2 schedule",
            "work 2 progress",
            "work 3 schedule",
            "work 3 progress",
            "work 4 schedule",
            "work 4 progress",
            "work 5 schedule",
            "work 5 progress",
            "work 6 schedule",
            "work 6 progress"
        )
        val numRows = rowHeadings.size
        val numCols = columnHeadings.size
        checkboxStates = Array(numRows) { IntArray(numCols * 4) }
        // Example checkboxStates array
        val checkboxStates = arrayOf(
            intArrayOf(1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
        createDynamicTable(view, numRows, numCols, columnHeadings, rowHeadings, checkboxStates)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize plannedDate and systemDate with your date values
        plannedDate = parseDate("yyyy-MM-dd", "2023-08-10") // Replace with your planned date
        systemDate = Date() // Get the current system date
        enableColumnsBasedOnDate()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
