package com.example.pwd_app.viewModel.schoolDisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.R
import com.example.pwd_app.data.remote.ApiInterface
import com.example.pwd_app.data.remote.ApiUtility
import com.example.pwd_app.model.Schools

class SchoolDisplay : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var schoolDisplayViewModel: SchoolDisplayViewModel
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.history_schools, container, false)

        val apiInterface = ApiUtility.getInstance().create(ApiInterface::class.java)
        schoolDisplayViewModel = ViewModelProvider(
            this,
            SchoolDisplayViewModelFactory(apiInterface)
        )[SchoolDisplayViewModel::class.java]
        recyclerView = view.findViewById(R.id.schoolRecyclerView)

        // Set up RecyclerView layout manager and adapter
        val layoutManager = LinearLayoutManager(requireContext())
        // Initialize with an empty list

        recyclerView.layoutManager = layoutManager

        // Observe the LiveData and update the adapter
        schoolDisplayViewModel.schoolList.observe(viewLifecycleOwner) { data ->
            val groupedData = data.groupBy { it.school_name }

            // Create a list of Schools
            val schoolsList = groupedData.map { (schoolName, schoolDataList) ->
                val distinctImageNames = schoolDataList.map { it.image_name }.distinct()
                Schools(schoolName, distinctImageNames.joinToString())
            }
            recyclerView.adapter = SchoolAdapter(schoolsList)
        }


        // Fetch data when the fragment is created or as needed
        schoolDisplayViewModel.fetchSchoolData()

        return view
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}