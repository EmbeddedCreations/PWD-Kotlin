package com.example.pwd_app.viewModel.analytics

import com.example.pwd_app.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.model.WorkOrders

class WorkLog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_log)

        val headingTextView: TextView = findViewById(R.id.textViewHeading)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        //get serialized content from previous intent
        val workItems = intent.getSerializableExtra("workItem") as? ArrayList<WorkOrders>

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        // Create and set the adapter
        val adapter = CustomAdapter(workItems, this)
        recyclerView.adapter = adapter
    }
}

class CustomAdapter(private val dataList: ArrayList<WorkOrders>?, private val context: AppCompatActivity) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_wadapter, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



            holder.itemView.setOnClickListener {
                startFormActivity()
            }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var workOrderName: TextView = itemView.findViewById(R.id.workOrder)
        var contractor : TextView = itemView.findViewById(R.id.ContractorName)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun startFormActivity() {
        val intent = Intent(context, Form::class.java)
        context.startActivity(intent)
    }

}