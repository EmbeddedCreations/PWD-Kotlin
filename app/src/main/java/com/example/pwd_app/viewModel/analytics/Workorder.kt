package com.example.pwd_app.viewModel.analytics

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.R
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.WorkOrders
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class Workorder : AppCompatActivity() {
    private lateinit var workCardViewModel : WorkCardViewModel
    private var resultArray = mutableListOf<List<Any>>()
    private var uniqueDateRangesMap = HashMap<String, MutableList<WorkOrders>>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workorder)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        workCardViewModel = ViewModelProvider(
            this,
            WorkCardViewModelFactory(apiInterface)
        )[WorkCardViewModel::class.java]
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.parse("2023-01-02",formatter)
        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        workCardViewModel.fetchWorkOrders()
        workCardViewModel.workList.observe(this){ workItems ->
            for(workItem in workItems){
                val workStartDate = LocalDate.parse(workItem.WorkorderDate)
                val duration = workItem.DurationMonthsOrDays?.toLong() ?: 0L
                val dateRanges = generateDateRanges(currentDate,workStartDate,duration)

                for(dateRange in dateRanges) {
                    val dateRangeKey = "${dateRange.first} to ${dateRange.second}"

                    // Use putIfAbsent to ensure that if there are duplicates, only the first encountered workItem is associated with the date range
                    val workItemList = uniqueDateRangesMap.getOrPut(dateRangeKey) { mutableListOf() }

                    if (workItemList.none { it.SRN == workItem.SRN }) {
                        // Add the current workItem to the list associated with the key
                        workItemList.add(workItem)
                    }
                }
            }
            recyclerView.adapter = CustomAdapter2(uniqueDateRangesMap,this)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateDateRanges(currentDate : LocalDate, workStartDate : LocalDate, duration : Long) : List<Pair<LocalDate, LocalDate>>{
        val dateRanges = mutableListOf<Pair<LocalDate, LocalDate>>()
        var startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        while(startOfWeek.isBefore(currentDate)){
            val endOfWeek = startOfWeek.plusDays(6)
            val workRange = Pair(startOfWeek, endOfWeek)

            //check if the workOrder is within a range in the calendar
            if(isWorkInRange(workStartDate,duration,workRange)){
                dateRanges.add(workRange)
            }
            startOfWeek = startOfWeek.plusDays(7)
        }
        return dateRanges
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isWorkInRange(workStartDate : LocalDate, duration : Long, dateRange: Pair<LocalDate, LocalDate>) : Boolean{
        val workEndDate = workStartDate.plusDays(duration)
        return !(workEndDate.isBefore(dateRange.first) || workEndDate.isAfter(dateRange.second))
    }
}
class CustomAdapter2(private val dataList: HashMap<String, MutableList<WorkOrders>>, private val context: AppCompatActivity) :
    RecyclerView.Adapter<CustomAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_wadapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = dataList.entries.elementAt(position)
        val dateRange = entry.key
        val workItems = entry.value


        holder.textViewDateRange.text = dateRange
        for(workItem  in workItems){
            val textView = TextView(context)
            textView.text = workItem.WorkorderNumber
            holder.linearLayout.addView(textView)
        }
        // Set click listener only for the first item
        if (position == 0) {
            holder.itemView.setOnClickListener {
                // Handle click for the first item
                startFormActivity()
            }
        } else {
            holder.itemView.setOnClickListener {
                // Show a message for non-clickable items
                showToast("Please complete the first task")
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDateRange : TextView = itemView.findViewById(R.id.textViewDateRange)
        var linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayoutWorkOrder)
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun startFormActivity() {
        val intent = Intent(context, WorkLog::class.java)
        context.startActivity(intent)
    }

}