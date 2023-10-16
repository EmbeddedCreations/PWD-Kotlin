package com.example.pwd_app.viewModel.schoolDisplay

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.R
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.EditObject
import com.example.pwd_app.model.Schools
import com.example.pwd_app.viewModel.buildingDisplay.BuildingDisplay

class SchoolAdapter(
    private val schools: List<Schools>
) : RecyclerView.Adapter<SchoolAdapter.ViewHolder>() {


    // Define the ViewHolder class here
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views and bind data here
        val schoolName: TextView = itemView.findViewById(R.id.SchoolNameTextView)
        val buildingNames: TextView = itemView.findViewById(R.id.BuildingNamesTextView)
        val schoolCard: FrameLayout = itemView.findViewById(R.id.schoolCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.school_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return schools.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = schools[position]
        val nameSchool = Credentials.schools.find { it.id == currentItem.school_name }?.school_name
        holder.schoolName.text = nameSchool
        holder.buildingNames.text = currentItem.buildings
        holder.schoolCard.setOnClickListener {
            //Intent to Start a New Activity
            //i want to get the context and startActivity
            Credentials.SELECTED_SCHOOL_FOR_DISPLAY = currentItem.school_name.toString()
            EditObject.E_SCHOOL_NAME = currentItem.school_name.toString()
            val context = holder.itemView.context
            // Create and start the intent
            val intent = Intent(context, BuildingDisplay::class.java)
            context.startActivity(intent)
        }

    }
}