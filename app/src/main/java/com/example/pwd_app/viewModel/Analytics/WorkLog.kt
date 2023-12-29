package com.example.pwd_app.viewModel.Analytics
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

class WorkLog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_log)

        val headingTextView: TextView = findViewById(R.id.textViewHeading)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Sample data for the RecyclerView
        val dataList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

        // Create and set the adapter
        val adapter = CustomAdapter(dataList, this)
        recyclerView.adapter = adapter
    }
}

class CustomAdapter(private val dataList: List<String>, private val context: AppCompatActivity) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemText = dataList[position]
        holder.itemTextView.text = itemText

            holder.itemView.setOnClickListener {
                startFormActivity()
            }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.textViewItem)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun startFormActivity() {
        val intent = Intent(context, Workorder::class.java)
        context.startActivity(intent)
    }
}