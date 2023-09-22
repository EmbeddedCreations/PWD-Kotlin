package com.example.pwd_app.viewModel.localDbView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pwd_app.R
import com.example.pwd_app.data.local.DatabaseHelper
import com.example.pwd_app.repository.LocalDatabaseRepository

class LocalDbView : Fragment() {
    private lateinit var localDbViewModel: LocalDbViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_loacaldb, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val database = DatabaseHelper.getDatabase(requireContext())
        val localDatabaseRepository = LocalDatabaseRepository(database)
        localDbViewModel = ViewModelProvider(
            this,
            LocalDbViewModelFactory(localDatabaseRepository)
        )[LocalDbViewModel::class.java]

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.localDbRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        localDbViewModel.surveyData.observe(viewLifecycleOwner) { savedData ->
            recyclerView.adapter = LocalDbAdapter(savedData)
        }
    }
}
