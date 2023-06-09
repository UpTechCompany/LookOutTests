package com.example.uptechapp.activity;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;

import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import com.example.uptechapp.R;
import com.example.uptechapp.databinding.ActivityMainFragmentsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivityFragments extends AppCompatActivity {

    private ActivityMainFragmentsBinding binding;
    public String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();


    }



    private void init() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragmentContainer);
        NavController navController = navHostFragment.getNavController();

        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navController.navigate(item.getItemId());
                return true;
            }
        });
    }
}