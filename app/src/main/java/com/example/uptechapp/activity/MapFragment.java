package com.example.uptechapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.uptechapp.R;
import com.example.uptechapp.api.CompleteListener;
import com.example.uptechapp.dao.Database;
import com.example.uptechapp.dao.MapService;
import com.example.uptechapp.dao.MyViewModel;
import com.example.uptechapp.databinding.FragmentMapBinding;
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;

    private List<Emergency> myEmergencyList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMapBinding.inflate(getLayoutInflater());

        myEmergencyList = new ArrayList<Emergency>();
        Database.loadEmergencies(new CompleteListener() {
            @Override
            public void OnSuccess() {
            }

            @Override
            public void OnFailure() {
            }
        });

        final Observer<List<Emergency>> myObserver = new Observer<List<Emergency>>() {
            @Override
            public void onChanged(List<Emergency> emergencies) {
                Log.d("NIKITA", "INOF");
                Log.d("NIKITA", String.valueOf(emergencies.size()));
                myEmergencyList.clear();
                myEmergencyList.addAll(emergencies);
            }
        };
        MyViewModel.getInstance().getEmergencyLiveData().observe(this, myObserver);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(new MapService(getContext(), this));
        MapService mapService = new MapService(getContext(), this);
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
}
