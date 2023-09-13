package com.example.pwd_app.viewModel.upload

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.pwd_app.R
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Credentials
import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.UploadObject
import com.example.pwd_app.network.NetworkStatusUtility
import com.example.pwd_app.repository.DataRepository
import com.example.pwd_app.viewModel.localDbView.LocalDbViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale


class UploadFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var uploadViewModel : UploadViewModel

    private val CAMERA_CODE = 101
    private val RQS_OPEN_IMAGE = 1
    private val INITIAL_IMAGE_RESOURCE = R.drawable.uploadfile

    private var encodedImage: String = ""
    private var gpsLatitude = 0.0
    private var gpsLongitude = 0.0
    private var dateTaken: Date? = null
    private var timeTaken: Date? = null
    private var date_today: String? = null
    private var time_today: String? = null
    private var textUri: TextView? = null
    private var pickImageButton: Button? = null
    private var buttonUploadImage: Button? = null
    private var buttonSaveImage: Button? = null
    private var editTextDescription: EditText? = null
    private var progressDialog: ProgressDialog? = null
    private var networkStatusUtility: NetworkStatusUtility? = null
    private var iv_imgView: ImageView? = null
    private var status: ImageView? = null
    private var targetUri: Uri? = null
    private var imageChanged = false
    private var textView: TextView? = null

    private val selectedIssuesList: MutableList<String> = mutableListOf()
    private val issueArray = arrayOf(
        "Snake", "Grass", "Mud", "rodents", "Insects", "Mosquitoes"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_upload, container, false)
        // Initialize views and perform other setup
        return view
    }

    private val textUriOnClickListener = View.OnClickListener {
        if (targetUri != null) {
            val bm: Bitmap
            try {
                bm = BitmapFactory.decodeStream(
                    requireContext().contentResolver
                        .openInputStream(targetUri!!)
                )
                iv_imgView!!.setImageBitmap(bm)
                //encodeBitmap(bm)
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        val database = DatabaseHelper.getDatabase(requireContext())
        val dataRepository = DataRepository(apiInterface)
        uploadViewModel = ViewModelProvider(this,UploadViewModelFactory(dataRepository)).get(UploadViewModel::class.java)

        status = requireView().findViewById(R.id.statusIcon)
        iv_imgView = requireView().findViewById(R.id.image_view)
        pickImageButton = requireView().findViewById(R.id.pickimage)
        buttonSaveImage = requireView().findViewById(R.id.buttonSaveImage)
        status = requireView().findViewById(R.id.statusIcon)
        val textViewLoggedIn = requireView().findViewById<TextView>(R.id.textViewLoggedIn)
        textUri = requireView().findViewById<TextView>(R.id.Dimensions)
        textUri?.setOnClickListener(textUriOnClickListener)
        editTextDescription = requireView().findViewById(R.id.editTextDescription)

        editTextDescription?.isEnabled = false
        val juniorEngineer: String = Credentials.DEFAULT_JUNIOR_ENGINEER
        textViewLoggedIn.text = "Logged in as: $juniorEngineer"

        buttonUploadImage = requireView().findViewById<Button>(R.id.buttonUploadImage)
        buttonUploadImage?.isEnabled = false
        buttonUploadImage?.alpha = 0.5f

        networkStatusUtility = NetworkStatusUtility(requireContext())
        if (networkStatusUtility!!.isNetworkAvailable) {
            status?.setImageResource(R.drawable.online)
        } else {
            status?.setImageResource(R.drawable.offline)
        }

        networkStatusUtility!!.startMonitoringNetworkStatus(object : NetworkStatusUtility.NetworkStatusListener {
            override fun onNetworkAvailable() {
                requireActivity().runOnUiThread {
                    status?.setImageResource(R.drawable.online)
                    buttonUploadImage?.isEnabled = true
                    buttonUploadImage?.alpha = 1.0f
                    status?.setOnClickListener(View.OnClickListener { showToast("Online") })
                }
            }

            override fun onNetworkLost() {
                requireActivity().runOnUiThread {
                    status?.setImageResource(R.drawable.offline)
                    buttonUploadImage?.isEnabled = false
                    buttonUploadImage?.alpha = 0.5f
                    status?.setOnClickListener(View.OnClickListener { showToast("Offline") })
                }
            }
        })

        buttonUploadImage?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!networkStatusUtility!!.isNetworkAvailable) {
                    Toast.makeText(
                        requireContext(),
                        "No internet connection available",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val description = editTextDescription?.text.toString().trim()
                if (description.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter a description.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (iv_imgView?.drawable == null) {
                    Toast.makeText(
                        requireContext(),
                        "Please select an image first.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!imageChanged) {
                    Toast.makeText(
                        requireContext(),
                        "Please select an image first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Initiase Values
                    progressDialog = ProgressDialog(requireContext())
                    progressDialog?.setTitle("Uploading Image")
                    progressDialog?.setMessage("Please wait...")
                    progressDialog?.show()
                    buttonUploadImage!!.isEnabled = false
                    var Tags = Arrays.toString(selectedIssuesList.toTypedArray())
                    Tags = Tags.substring(1, Tags.length - 1)
                    val finalTags = Tags
                    val bitmap = (iv_imgView!!.getDrawable() as BitmapDrawable).bitmap
                    encodeBitmap(bitmap)

                    UploadObject.DESCRIPTION = description
                    UploadObject.AGS = finalTags
                    UploadObject.LONGITUDE = gpsLongitude.toString()
                    UploadObject.LATITUDE = gpsLatitude.toString()
                    UploadObject.UPLOAD_DATE = date_today.toString()
                    UploadObject.UPLOAD_TIME = time_today.toString()
                    UploadObject.IMAGE_PDF = encodedImage
                    UploadObject.IMAGE_TYPE="jpg"
                    uploadViewModel.uploadData(
                         UploadObject.SCHOOL_NAME,
                         UploadObject.PO_OFFICE,
                         UploadObject.IMAGE_NAME,
                         UploadObject.IMAGE_TYPE,
                         UploadObject.IMAGE_PDF,
                         UploadObject.UPLOAD_DATE,
                         UploadObject.UPLOAD_TIME,
                         UploadObject.ENTRYBY,
                         UploadObject.LATITUDE,
                         UploadObject.LONGITUDE,
                         UploadObject.USER_UPLOAD_DATE,
                         UploadObject.INSPECTIONTYPE,
                         UploadObject.WORKORDERNUMBER,
                         UploadObject.DESCRIPTION,
                         UploadObject.AGS
                    )
                    uploadViewModel.uploadStatus.observe(viewLifecycleOwner){isUploaded->


                        progressDialog!!.dismiss()
                        // Re-enable the "Upload" button after the upload is completed
                        // Re-enable the "Upload" button after the upload is completed
                        buttonUploadImage!!.isEnabled = true
                        editTextDescription?.setText("")
                        iv_imgView?.setImageResource(INITIAL_IMAGE_RESOURCE)
                        selectedIssuesList.clear()
                        textView?.text = ""
                        imageChanged = false

                        Toast.makeText(
                            requireContext(),
                            "Uploaded Successfull",
                            Toast.LENGTH_SHORT
                        ).show()
//                        if (isUploaded) {
//                            // Reset UI elements on successful upload
//                            // Dismiss the progress dialog
//                            progressDialog!!.dismiss()
//                            // Re-enable the "Upload" button after the upload is completed
//                            // Re-enable the "Upload" button after the upload is completed
//                            buttonUploadImage!!.isEnabled = true
//                            editTextDescription?.setText("")
//                            iv_imgView?.setImageResource(INITIAL_IMAGE_RESOURCE)
//                            selectedIssuesList.clear()
//                            textView?.text = ""
//                            imageChanged = false
//
//                            Toast.makeText(
//                                requireContext(),
//                                "Uploaded Successfull",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            progressDialog!!.dismiss()
//                            // Re-enable the "Upload" button after the upload is completed
//                            // Re-enable the "Upload" button after the upload is completed
//                            buttonUploadImage!!.isEnabled = true
//                            Toast.makeText(
//                                requireContext(),
//                                "Uploaded Failed",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
                    }
                }
            }
        })

        buttonSaveImage?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val description = editTextDescription?.text.toString().trim()
                if (description.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter a description.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (iv_imgView?.drawable == null) {
                    Toast.makeText(
                        requireContext(),
                        "Please select an image first.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!imageChanged) {
                    Toast.makeText(
                        requireContext(),
                        "Please select an image first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var Tags = Arrays.toString(selectedIssuesList.toTypedArray())
                    Tags = Tags.substring(1, Tags.length - 1)
                    val finalTags = Tags
                    val bitmap = (iv_imgView!!.getDrawable() as BitmapDrawable).bitmap
                    encodeBitmap(bitmap)
                    UploadObject.DESCRIPTION = description
                    UploadObject.AGS = finalTags
                    UploadObject.LONGITUDE = gpsLongitude.toString()
                    UploadObject.LATITUDE = gpsLatitude.toString()
                    UploadObject.UPLOAD_DATE = date_today.toString()
                    UploadObject.UPLOAD_TIME = time_today.toString()
                    UploadObject.IMAGE_PDF = encodedImage
                    UploadObject.IMAGE_TYPE="jpg"
                    val imagedata = ImageData(
                        school_Name = UploadObject.SCHOOL_NAME,
                        po_office = UploadObject.PO_OFFICE,
                        image_name = UploadObject.IMAGE_NAME,
                        image_type = UploadObject.IMAGE_TYPE,
                        image_pdf =  UploadObject.IMAGE_PDF,
                        upload_date =UploadObject.UPLOAD_DATE,
                        upload_time =UploadObject.UPLOAD_TIME,
                        EntryBy = UploadObject.ENTRYBY,
                        Latitude =UploadObject.LATITUDE,
                        Longitude =UploadObject.LONGITUDE,
                        user_upload_date = UploadObject.USER_UPLOAD_DATE,
                        InspectionType = UploadObject.INSPECTIONTYPE,
                        WorkorderNumber = UploadObject.WORKORDERNUMBER,
                        Description = UploadObject.DESCRIPTION,
                        ags = UploadObject.AGS
                    )
                    lifecycleScope.launch {
                        //val newImageData = ImageData(/* your data here */)
                        val insertedRowId = database.Dao().insertIntoLocalDb(imagedata)

                        if (insertedRowId != -1L) {
                            editTextDescription?.setText("")
                            iv_imgView?.setImageResource(INITIAL_IMAGE_RESOURCE)
                            selectedIssuesList.clear()
                            textView?.text = ""
                            imageChanged = false

                            Toast.makeText(
                                requireContext(),
                                "Saved Successfull",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Insertion failed
                            // Handle the failure scenario here
                            Toast.makeText(
                                requireContext(),
                                "Saved Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        })


        textView = requireView().findViewById<TextView>(R.id.textViewTags)
        textView?.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select Major Problems")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                issueArray, null
            ) { dialogInterface, i, b ->
                if (b) {
                    selectedIssuesList.add(issueArray[i])
                } else {
                    selectedIssuesList.remove(issueArray[i])
                }
            }
            builder.setPositiveButton("OK") { dialogInterface, i ->
                textView?.text = TextUtils.join(", ", selectedIssuesList)
            }
            builder.setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            builder.setNeutralButton("Clear All") { dialogInterface, i ->
                selectedIssuesList.clear()
                textView?.text = ""
            }
            builder.show()
        })

        pickImageButton?.setOnClickListener(View.OnClickListener { showImageOptionsDialog() })
        iv_imgView?.setOnClickListener(View.OnClickListener { showImageOptionsDialog() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK || requestCode == ImagePicker.REQUEST_CODE) {
            val uri = data?.data
            editTextDescription!!.isEnabled = true
            targetUri = uri
            iv_imgView!!.setImageURI(uri)
            imageChanged = true
            showExif(targetUri)
            val dataUri = data?.data
            if (requestCode == RQS_OPEN_IMAGE) {
                targetUri = dataUri
                iv_imgView!!.setImageURI(uri)
                showExif(targetUri)
                imageChanged = true
            }
        }
    }

    private fun encodeBitmap(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteOfImages = byteArrayOutputStream.toByteArray()
        encodedImage = Base64.encodeToString(byteOfImages, Base64.DEFAULT)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // Not implemented yet
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Not implemented yet
    }

    private fun showExif(photoUri: Uri?) {
        if (photoUri != null) {
            try {
                requireContext().contentResolver.openFileDescriptor(photoUri, "r")
                    .use { parcelFileDescriptor ->
                        val fileDescriptor =
                            parcelFileDescriptor!!.fileDescriptor
                        val exifInterface = ExifInterface(fileDescriptor)
                        val latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                        val latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                        val longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                        val longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)

                        if (latitude != null && latitudeRef != null && longitude != null && longitudeRef != null) {
                            gpsLatitude = convertToDegree(latitude, latitudeRef)
                            gpsLongitude = convertToDegree(longitude, longitudeRef)
                        }

                        val datetime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
                        if (datetime != null) {
                            val dateFormat = SimpleDateFormat(
                                "yyyy:MM:dd HH:mm:ss",
                                Locale.getDefault()
                            )
                            try {
                                dateTaken = dateFormat.parse(datetime)
                                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                                timeTaken = timeFormat.parse(datetime.substring(11))
                                date_today = dateFormat.format(dateTaken)
                                time_today = timeFormat.format(timeTaken)
                            } catch (e: ParseException) {
                                e.printStackTrace()
                                dateTaken = null
                                timeTaken = null
                                date_today = null
                                time_today = null
                            }
                        }
                    }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    "Something wrong:\n$e",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun convertToDegree(coordinate: String, ref: String): Double {
        return try {
            val parts = coordinate.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val degreesParts = parts[0].split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val degrees = degreesParts[0].toDouble() / degreesParts[1].toDouble()
            val minutesParts = parts[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val minutes = minutesParts[0].toDouble() / minutesParts[1].toDouble()
            val secondsParts = parts[2].split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val seconds = secondsParts[0].toDouble() / secondsParts[1].toDouble()
            val result = degrees + minutes / 60.0 + seconds / 3600.0
            if (ref == "N" || ref == "E") result else -result
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            0.0
        }
    }

    private fun showToast(statusText: String) {
        Toast.makeText(requireContext(), statusText, Toast.LENGTH_SHORT).show()
    }

    private fun showImageOptionsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an option")
            .setItems(
                arrayOf<String>("Capture from Camera", "Select from Gallery")
            ) { dialog, which ->
                when (which) {
                    0 -> // "Capture from Camera" option is selected
                        ImagePicker.with(this@UploadFragment)
                            .cameraOnly()
                            .crop()
                            .compress(1024)
                            .maxResultSize(720, 720)
                            .start()

                    1 -> {
                        // "Select from Gallery" option is selected
                        ImagePicker.with(this)
                            .galleryOnly()
                            .crop()
                            .compress(1024)
                            .maxResultSize(720, 720)
                            .start();
                    }
                }
            }
        builder.show()
    }
}
