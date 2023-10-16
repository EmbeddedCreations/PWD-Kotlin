package com.example.pwd_app.viewModel.localDbView

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.R
import com.example.pwd_app.model.ImageData

class LocalDbAdapter(
    private val buildingList: List<ImageData>,

    ) : RecyclerView.Adapter<LocalDbAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<ImageView?>(R.id.imageView)
        var editView: ImageView = itemView.findViewById<ImageView?>(R.id.editButton)
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
        Log.d("Building List", buildingList.toString())
        val decodedImage: ByteArray = Base64.decode(currentItem.image_pdf, Base64.DEFAULT)

        // Create a Bitmap from the byte array
        val decodedBitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)


        holder.imageView.setImageBitmap(decodedBitmap)
        holder.descriptionTextView.text = "Description: " + currentItem.Description
        holder.Date.text = "Upload Date: " + currentItem.user_upload_date
        holder.BuildingName.text = "Building Name: " + currentItem.image_name
        holder.imageView.setOnClickListener {
            // Create a dialog to display the enlarged image
            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.dialog_enlarged_image)

            val enlargedImageView = dialog.findViewById<ImageView>(R.id.enlargedImageView)
            enlargedImageView.setImageBitmap(decodedBitmap)

            // Handle the close button click to dismiss the dialog
            val closeButton = dialog.findViewById<ImageButton>(R.id.closeImageButton)
            closeButton.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }
        holder.editView.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Edit Building is not allowed")
            alertDialogBuilder.setMessage("After uploading can edit in history") // Set your message here

            alertDialogBuilder.setPositiveButton("ok") { dialog, which ->
                dialog.dismiss()

            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

    }
}