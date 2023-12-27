package com.example.pwd_app.viewModel

import com.example.pwd_app.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val dataList: List<String>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemText = dataList[position]
        holder.itemTextView.text = itemText

        // Set click listener only for the first item
        if (position == 0) {
            holder.itemView.setOnClickListener {
                // Handle click for the first item
                // Add your logic here
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
        val itemTextView: TextView = itemView.findViewById(R.id.textViewItem)
    }

    private fun showToast(message: String) {
        // Implement your logic to show a toast message
    }
}

