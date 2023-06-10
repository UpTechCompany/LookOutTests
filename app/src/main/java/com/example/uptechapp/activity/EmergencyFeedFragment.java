package com.example.uptechapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

public class EmergencyFeedFragment extends Fragment {

    private FragmentEmergencyFeedBinding binding;

    private RecyclerView emergencyFeed;

    private Dialog progressBar;
    private TextView dialogText;

    private long learn = 0;

    List<Emergency> myEmergencyList;
    EmergencyAdapter adapter;

    public Dialog Dialog(@Nullable Bundle savedInstanceState) {
        String title = getResources().getString(R.string.training);
        String message = getResources().getString(R.string.massage);
        String button1String = getResources().getString(R.string.yes);
        String button2String = getResources().getString(R.string.no);
        final int[] par = {0};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        Dialog dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.learning);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                LearnFragment learnFragment = new LearnFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragmentContainer, learnFragment);
                fragmentTransaction.addToBackStack(null);
                requireActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
                fragmentTransaction.commit();
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setCancelable(true);

        return builder.show();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentEmergencyFeedBinding.inflate(getLayoutInflater());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        learn = sharedPref.getInt(getString(R.string.learn), 0);
        if (learn == 0){
            Dialog(null);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("Learn", 1);
        }
        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        emergencyFeed.setLayoutManager(layoutManager);

        myEmergencyList = new ArrayList<Emergency>();
        adapter = new EmergencyAdapter(myEmergencyList, getContext(), getActivity());

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
        dialogText.setText(getResources().getString(R.string.load));

        progressBar.show();
    }
}
