package com.example.uptechapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup;

import com.example.uptechapp.R;

import java.util.Locale;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "ActivitySplash";
    public static String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Thread(() -> {
            updateLocale();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long id = 0L;
            try {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                id = sharedPref.getLong(getString(R.string.id_logging), 0L);

            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }

            Intent intent;
//            if (id != 0l){
//                intent = new Intent(SplashActivity.this, MainActivityFragments.class);
//            }
//            else {
//                intent = new Intent(SplashActivity.this, LoginActivity.class);
//            }
            intent = new Intent(SplashActivity.this, MainActivityFragments.class);
            startActivity(intent);
            SplashActivity.this.finish();

        }).start();


    }
    private void updateLocale() {
        String language = Locale.getDefault().getLanguage();
        SplashActivity.language = language;
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

    }

    public static String getLanguage() {
        return language;
    }
}