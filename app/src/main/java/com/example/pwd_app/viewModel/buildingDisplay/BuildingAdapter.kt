package com.example.pwd_app.viewModel.buildingDisplay

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.R
import com.example.pwd_app.model.SchoolData
import com.squareup.picasso.Picasso

class BuildingAdapter(
    private val buildingList: List<SchoolData>
) : RecyclerView.Adapter<BuildingAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<ImageView?>(R.id.imageView)
        var descriptionTextView: TextView =
            itemView.findViewById<TextView>(R.id.buildingDescTextView)
        var BuildingName: TextView = itemView.findViewById(R.id.buildingNameTextView)
        var Date: TextView = itemView.findViewById(R.id.buildingDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_description, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return buildingList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = buildingList[position]
        Picasso.get()
            .load(currentItem.image_pdf)
            .placeholder(R.drawable.uploadfile) // Placeholder image from drawable
            .error(R.drawable.imgnotfound) // Image to show if loading from URL fails
            .into(holder.imageView)
        // holder.Picasso.get().load(model.getImageUrl()).into(holder.imageView);
        holder.descriptionTextView.text = "Description: " + currentItem.Description
        holder.Date.text = "Upload Date: " + currentItem.user_upload_date
        holder.BuildingName.text = "Building Name: " + currentItem.image_name
        holder.imageView.setOnClickListener {
            // Create a dialog to display the enlarged image
            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.dialog_enlarged_image)
            val enlargedImageView =
                dialog.findViewById<ImageView>(R.id.enlargedImageView)
            Picasso.get()
                .load(currentItem.image_pdf)
                .placeholder(R.drawable.uploadfile)
                .error(R.drawable.imgnotfound)
                .into(enlargedImageView)

            // Handle the close button click to dismiss the dialog
            val closeButton = dialog.findViewById<ImageButton>(R.id.closeImageButton)
            closeButton.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }
}