package com.example.uptechapp.dao;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.uptechapp.R;
import com.example.uptechapp.activity.CreateEmergencyFragment;
import com.example.uptechapp.activity.EmergencyFeedFragment;
import com.example.uptechapp.activity.MainActivityFragments;
import com.example.uptechapp.activity.MapFragment;
import com.example.uptechapp.activity.SplashActivity;
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener{

    private static final String TAG = "MapService";
    private final Context context;
    private LocationManager locationManager;

    private LifecycleOwner lifecycleOwner;
    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Emergency> myEmergencyList;



    public MapService(Context context, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        myEmergencyList = MyViewModel.getInstance().getEmergencyLiveData().getValue();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(context, latLng.latitude + " "
                + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
//        Toast.makeText(context, "" + latLng.latitude + " "
//                + latLng.longitude, Toast.LENGTH_SHORT).show();
//
//        FragmentManager fragmentManager = getSupportedManager().findFragmentById(R.id.fragmentContainerView);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment fragment = new CreateEmergencyFragment();
//        CreateEmergencyFragment.setLatitude(latLng.latitude);
//        CreateEmergencyFragment.setLongitude(latLng.longitude);
//        fragmentTransaction.add(R.id.fragmentContainerView, fragment);
//        fragmentTransaction.commit();
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.fragment_create_emergency);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
//        Log.d("Nike", "Ok");
//        Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.fragment_create_emergency);
//        dialog.show();
//        Log.d("Nike", "Ok");
//        FragmentContainerView fragmentContainerView = dialog.findViewById(R.id.fragmentContainerView);
//        CreateEmergencyFragment createEmergencyFragment = new CreateEmergencyFragment();
//        Log.d("Nike", "Ok");
////        FragmentManager fragmentManager = createEmergencyFragment.getChildFragmentManager();
////        Log.d("Nike", "Ok");
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        Log.d("Nike", "Ok");
////        fragmentTransaction.replace(fragmentContainerView.getId(), createEmergencyFragment);
////        fragmentTransaction.addToBackStack(null);
////        Log.d("Nike", "Ok");
////        fragmentTransaction.commit();
////        Log.d("Nike", "Ok");












    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: READY");
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), 15));
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude())).title("Текущее местоположение"));
        List<Emergency> myEmergencyList = MyViewModel.getInstance().getEmergencyLiveData().getValue();
        //Log.i("qq", "myEmergencyList" + myEmergencyList.toString());

        Log.d(TAG, "onMapReady: check before load emergencies");

        //assert myEmergencyList != null;
//        for (Emergency emergency: myEmergencyList) {
//            emergency.setLocation(emergency.getLattitude(), emergency.getLongitude());
//            googleMap.addMarker(new MarkerOptions().position(emergency.getLocation()).title(emergency.getTitle()));
//            Log.d(TAG, "OnSuccess: add emergency");
//        }

//        googleMap.setOnMarkerClickListener(marker -> {
//            Log.d(TAG, "OnSuccess: markerclicklistener");
//            Emergency emergency = Database.getEmergencyByTitle(marker.getTitle());
//
//            Dialog dialog = new Dialog(context);
//            dialog.setContentView(R.layout.dialog_fragment);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setGravity(Gravity.BOTTOM);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//
//            TextView tv_name = dialog.getWindow().findViewById(R.id.tv_name);
//            TextView tv_time = dialog.getWindow().findViewById(R.id.tv_time);
//            TextView tv_info = dialog.getWindow().findViewById(R.id.tv_description);
//
//            tv_name.setText(emergency.getTitle());
//            tv_info.setText(emergency.getDescription());
//            tv_time.setText(emergency.getTime().toString());
//
//
//            ImageView imageView = dialog.getWindow().findViewById(R.id.iv_image);
//            StorageReference reference = FirebaseStorage.getInstance().getReference(emergency.getPhotoUrl());
//            Glide.with(context).load(reference).into(imageView);
//
//            return false;
//        });
            final Observer<List<Emergency>> myObserver = new Observer<List<Emergency>>() {
            @Override
            public void onChanged(List<Emergency> emergencies) {
                Log.d("NIKITA", "INOF");
                //Log.d("NIKITA", String.valueOf(emergencies.size()));
                //Move the camera to the user's location and zoom in!
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CreateEmergencyFragment.getLatitude(), CreateEmergencyFragment.getLongitude()), 12.0f));
                myEmergencyList.clear();
                myEmergencyList.addAll(emergencies);
            }


        };
        MyViewModel.getInstance().getEmergencyLiveData().observe(lifecycleOwner, myObserver);
        Log.d(TAG, "onMapReady: proehali");
    }
}
