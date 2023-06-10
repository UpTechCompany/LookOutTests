package com.example.uptechapp.dao;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.example.uptechapp.R;
import com.example.uptechapp.api.EmergencyApiService;
import com.example.uptechapp.api.PickImage;
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener{

    private static final String TAG = "MapService";
    private final Context context;
    private LocationManager locationManager;

    private LifecycleOwner lifecycleOwner;
    private Activity activity;
    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Emergency> myEmergencyList;
    private Uri uriImage;
    private StorageReference storageReference;
    private ActivityResultLauncher<String> mGetContent;

    private TextView editTextLabel;
    private Button btnChoose;
    private TextView editTextDesc;
    private Button btnShare;
    private ImageView emergencyImg;
    private PickImage pickImage;
    private LatLng location;

    public MapService(Context context, LifecycleOwner lifecycleOwner, Activity activity, ActivityResultLauncher<String> mGetContent, PickImage pickImage) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        myEmergencyList = MyViewModel.getInstance().getEmergencyLiveData().getValue();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.activity = activity;
        storageReference = FirebaseStorage.getInstance().getReference("Emergency");
        this.mGetContent = mGetContent;
        this.pickImage = pickImage;
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
        location = latLng;

        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.fragment_create_emergency);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();

        editTextLabel = dialog.getWindow().findViewById(R.id.editTextLabel);
        btnChoose = dialog.getWindow().findViewById(R.id.btnChoosePicture);
        editTextDesc = dialog.getWindow().findViewById(R.id.editTextDescription);
        btnShare = dialog.getWindow().findViewById(R.id.btnShare);
        emergencyImg = dialog.getWindow().findViewById(R.id.emergencyImg);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareEmergency();
            }
        });

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

    private void openFileChooser() {
        mGetContent.launch("image/*");
//        pickImage.pickImage();
    }

    public void setImage(Uri uri) {
        uriImage = uri;
        emergencyImg.setImageURI(uriImage);
    }

    private String getFileExtension(Uri uriImage) {
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uriImage));
    }

    private void shareEmergency() {
        if (uriImage != null) {

            int id = 1;

            StorageReference fileReference = storageReference.child(String.valueOf(id) + "/Photo." + getFileExtension(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;

                            String url = downloadUri.toString();
                            String[] time = Calendar.getInstance().getTime().toString().split(" ");
                            Log.i("time", "Time" + Arrays.toString(time));

                            Emergency emergency = new Emergency(
                                    "-1",
                                    editTextLabel.getText().toString(),
                                    editTextDesc.getText().toString(),
                                    Calendar.getInstance().getTime().toString(),
                                    url,
                                    location.latitude,
                                    location.longitude
                            );

                            EmergencyApiService.getInstance().postJson(emergency).enqueue(new Callback<Emergency>() {
                                @Override
                                public void onResponse(@NonNull Call<Emergency> call, @NonNull Response<Emergency> response) {
                                    Log.i(TAG, "Response - " + call.toString());
                                }

                                @Override
                                public void onFailure(@NonNull Call<Emergency> call, @NonNull Throwable t) {
                                    Log.i(TAG, "FAIL - " + t.getMessage());
                                }
                            });

                        }
                    });
                    Navigation.findNavController(activity, R.id.mainFragmentContainer).navigate(R.id.fragment_emergency_feed);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "File was not selected", Toast.LENGTH_SHORT).show();
        }
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