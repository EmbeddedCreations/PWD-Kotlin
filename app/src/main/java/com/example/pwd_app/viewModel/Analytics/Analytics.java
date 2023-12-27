package com.example.pwd_app.viewModel.Analytics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.pwd_app.R;
import com.example.pwd_app.viewModel.WorkLog;

public class Analytics extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_analytics, container, false);

        // Find the button by its ID
        Button goButton = view.findViewById(R.id.gobutton);

        // Set click listener for the button
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Log class
                Intent intent = new Intent(getActivity(), WorkLog.class);

                // Start the Log activity
                startActivity(intent);
            }
        });

        return view;
    }
}
