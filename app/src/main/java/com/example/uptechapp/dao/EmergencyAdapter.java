package com.example.uptechapp.dao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uptechapp.R;
import com.example.uptechapp.model.Emergency;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private List<Emergency> emergenciesList;
    static final String TAG = "AdapterEmergency";
    private Context context;
    Activity activity;

    public EmergencyAdapter(List<Emergency> emergenciesList, Context context, Activity activity) {
        Log.d(TAG, "EmergencyAdapter: " + emergenciesList);
        this.emergenciesList = emergenciesList;
        this.context = context;
        this.activity = activity;
        Log.d(TAG, "EmergencyAdapter: CREATE");
    }

    @NonNull
    @Override
    public EmergencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emergency2, parent, false);
        Log.d(TAG, "onCreateViewHolder: return");
        return new EmergencyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: start");
        Emergency emergency = emergenciesList.get(position);

        Log.d(TAG, "onBindViewHolder: go");
        Log.i(TAG, "Emergency - " + emergency.getTitle());

        holder.setData(
                emergency.getTitle(),
                emergency.getDescription(),
                emergency.getTime(),
                emergency.getPhotoUrl(),
                emergency.getLattitude(),
                emergency.getLongitude()
        );
    }

    @Override
    public int getItemCount() {
        return emergenciesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView emergencyTitle, emergencyDescription, emergencyTime;
        private final ImageView emergencyPhoto;
        private final Button emergencyMapButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emergencyTitle = itemView.findViewById(R.id.textLabel);
            emergencyDescription = itemView.findViewById(R.id.textDescription);
            emergencyTime = itemView.findViewById(R.id.textDate);
            emergencyPhoto = itemView.findViewById(R.id.imageView);
            emergencyPhoto.setClipToOutline(true);
            emergencyMapButton = itemView.findViewById(R.id.emergency_map_button);

        }

        private void setData(String title, String description, String time, String photo, Double lattitude, Double longitude) {

        try {
            TimeZone userTimeZone = TimeZone.getDefault();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatLocal.setTimeZone(userTimeZone);
            String dateLocal = dateFormatLocal.format(time);
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lattitude, longitude, 1);
            String fullAddress = " ";
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                fullAddress = address.getAddressLine(0);
            }


            emergencyTitle.setText(title);
            emergencyDescription.setText(description);
            emergencyTime.setText(dateLocal);
            emergencyMapButton.setText(fullAddress);
            emergencyMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(activity, R.id.mainFragmentContainer).navigate(R.id.google_map);
                }
            });
//            emergencyPhoto.setImageResource(R.drawable.ic_google_logo);
            Glide.with(context).load(photo).into(emergencyPhoto);
         } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }
}
