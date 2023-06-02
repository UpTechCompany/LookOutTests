package com.example.uptechapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uptechapp.R;
import com.example.uptechapp.api.CompleteListener;
import com.example.uptechapp.dao.Database;
import com.example.uptechapp.dao.EmergencyAdapter;
import com.example.uptechapp.dao.MyViewModel;
import com.example.uptechapp.databinding.FragmentEmergencyFeedBinding;
import com.example.uptechapp.model.Emergency;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class EmergencyFeedFragment extends Fragment {

    private FragmentEmergencyFeedBinding binding;

    private RecyclerView emergencyFeed;
    private Dialog progressBar;
    private TextView dialogText;

    List<Emergency> myEmergencyList;
    EmergencyAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentEmergencyFeedBinding.inflate(getLayoutInflater());

        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        emergencyFeed.setLayoutManager(layoutManager);

        myEmergencyList = new ArrayList<Emergency>();
        adapter = new EmergencyAdapter(myEmergencyList, getContext());

        emergencyFeed.setAdapter(adapter);

        Database.loadEmergencies(new CompleteListener() {
            @Override
            public void OnSuccess() {
                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                progressBar.dismiss();

            }
        });

        final Observer<List<Emergency>> myObserver = new Observer<List<Emergency>>() {
            @Override
            public void onChanged(List<Emergency> emergencies) {
                myEmergencyList.clear();
                myEmergencyList.addAll(emergencies);
                adapter.notifyDataSetChanged();
            }
        };
        MyViewModel.getInstance().getEmergencyLiveData().observe(this, myObserver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void init() {

        emergencyFeed = binding.emergencyFeed;

        progressBar = new Dialog(getContext());
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");

        progressBar.show();
    }
}
