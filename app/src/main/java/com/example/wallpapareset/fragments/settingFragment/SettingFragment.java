package com.example.wallpapareset.fragments.settingFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallpapareset.databinding.FragmentSettingBinding;
import com.example.wallpapareset.fragments.settingFragment.adapter.DownloadAdapter;

import java.io.File;

public class SettingFragment extends Fragment {

    private static final String TAG = "Kia--SettingFragment----> ";
    private FragmentSettingBinding binding;
    private DownloadAdapter downloadAdapter;
    private boolean empty = false;
    private String alarm;
    private static int REQUEST_CODE = 100;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            creatList();
        } else {
            askPermision();
        }

        if (empty){
            binding.alarm.setText(alarm);
            binding.a.setVisibility(View.INVISIBLE);
            binding.alarmLayout.setVisibility(View.VISIBLE);
            Log.d(TAG, "empty: " + empty);
        }else {
            binding.a.setVisibility(View.VISIBLE);
            binding.alarmLayout.setVisibility(View.INVISIBLE);
            Log.d(TAG, "empty: " + empty);


        }

        binding.buttonPermisions.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);

        });




        return binding.getRoot();
    }


    @SuppressLint("LongLogTag")
    private void askPermision() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        empty = true;
        alarm = "باید مجوز دسترسی به فایل هارا فعال کنید";
        binding.buttonPermisions.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "برای دانلود عکس باید مجوز دسترسی فعال باشد.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "3: ");
    }

    @SuppressLint("LongLogTag")
    private void creatList() {
        Log.d(TAG, "4: ");

        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/DCIM", "wall");
        dir.mkdirs();


        File[] listFile = dir.listFiles();


            if (listFile.length == 0){
                empty = true;
                alarm = "دانلودی وجود ندارد";
                binding.buttonPermisions.setVisibility(View.INVISIBLE);
                Log.d(TAG, "5: ");
            }else{
                LinearLayoutManager horizColors = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
                binding.includedSetting.recAll.setLayoutManager(horizColors);
                downloadAdapter =  new DownloadAdapter(getActivity(),listFile);
                binding.includedSetting.recAll.setAdapter(downloadAdapter);
                downloadAdapter.notifyDataSetChanged();
                Log.d(TAG, "listFile: -> "+ listFile.length);
                Log.d(TAG, "6: ");

                empty = false;
            }




        
    }


}