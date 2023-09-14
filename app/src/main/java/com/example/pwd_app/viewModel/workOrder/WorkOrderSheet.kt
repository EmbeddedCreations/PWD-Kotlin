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

class WorkOrderSheet : Fragment(),AdapterView.OnItemSelectedListener {

    private lateinit var checkboxStates: Array<IntArray>

    private fun checkMatchingScheduleAndProgress(checkboxStates: Array<IntArray>) {
        val tableLayout = requireView().findViewById<TableLayout>(R.id.tableLayout)
        val numRows = tableLayout.childCount
        var i = 1
        while (i < numRows) {
            val scheduleRow = tableLayout.getChildAt(i) as TableRow
            val progressRow = tableLayout.getChildAt(i + 1) as TableRow
            var allMatch = true // Assume all checkboxes match
            for (j in 1 until progressRow.childCount) {
                val scheduleCheckBox = scheduleRow.getChildAt(j) as CheckBox
                val progressCheckBox = progressRow.getChildAt(j) as CheckBox
                val scheduleChecked = scheduleCheckBox.isChecked
                val progressChecked = progressCheckBox.isChecked
                if (scheduleChecked != progressChecked) {
                    // If at least one checkbox doesn't match, set allMatch to false and break
                    allMatch = false
                    break
                }
            }
            if (allMatch) {
                // If all checkboxes match, set the background color of progressRow to transparent
                progressRow.setBackgroundColor(Color.TRANSPARENT)
            } else {
                // If any checkbox doesn't match, set the background color of progressRow to red
                progressRow.setBackgroundColor(Color.RED)
            }
            i += 2
        }
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
                        checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
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
                            checkMatchingScheduleAndProgress(checkboxStates)
                            // Update the corresponding value in the checkboxStates array
                            val rowIndex = tableLayout.indexOfChild(row)
                            val checkboxArrayIndex = (columnIndex - 1) * 4 + k
                            checkboxStates[rowIndex - 1][checkboxArrayIndex] =
                                if (isChecked) 1 else 0

                            // Print the updated array to the console
                            for (i in 0 until numRows) {
                                for (j in 0 until numCols*4) {
                                    print(checkboxStates[i][j].toString() + " ")
                                }
                                println() // Move to the next row
                            }
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


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}