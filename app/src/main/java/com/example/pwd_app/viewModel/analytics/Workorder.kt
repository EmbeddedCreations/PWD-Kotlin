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
import com.example.pwd_app.model.WorkorderLog
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
       
        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        workCardViewModel.fetchWorkOrders()
        workCardViewModel.workList.observe(this){ workItems ->

//            recyclerView.adapter = CustomAdapter2(uniqueDateRangesMap,this)
        }
    }

}
class CustomAdapter2(private val dataList: List<WorkorderLog>, private val context: AppCompatActivity) :
    RecyclerView.Adapter<CustomAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = dataList[position]

        val dateString = entry.weekStartDate + "to" + entry.weekEndDate
        holder.textViewDateRange.text = dateString

        val textView = TextView(holder.itemView.context)
        textView.text = entry.contractorName
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holder.linearLayout.addView(textView)
        textView.text = entry.workOrderName
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holder.linearLayout.addView(textView)
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
        val intent = Intent(context, Form::class.java)
        context.startActivity(intent)
    }

}