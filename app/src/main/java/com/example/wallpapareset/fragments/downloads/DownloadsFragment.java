package com.example.wallpapareset.fragments.downloads;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallpapareset.databinding.FragmentDownloadBinding;
import com.example.wallpapareset.fragments.downloads.adapter.DownloadAdapter;
import com.example.wallpapareset.fragments.home.ListsViewModel;

import java.io.File;

public class DownloadsFragment extends Fragment {

    private static final String TAG = "Kia--SettingFragment----> ";
    private FragmentDownloadBinding binding;
    private DownloadAdapter downloadAdapter;
    private DownloadViewModel downloadViewModel;
    private boolean empty = false;
    private boolean per = false;
    private String alarm;
    private static int REQUEST_CODE = 100;
    Handler h = new Handler();
    Runnable r;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDownloadBinding.inflate(inflater, container, false);
        downloadViewModel = new ViewModelProvider(requireActivity()).get(DownloadViewModel.class);
        per = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        binding.includedSetting.progressHorizontal.setVisibility(View.GONE);

//        ////////////////////----------~~ <onBackPress> ~~----------////////////////////
//
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
//
//        ////////////////////----------~~ <onBackPress> ~~----------////////////////////


        binding.a.setVisibility(View.INVISIBLE);

        r = () -> {

            if (per) {

                creatList();
            } else {
                askPermision();
            }

            if (empty) {
                binding.alarm.setText(alarm);
                binding.a.setVisibility(View.INVISIBLE);
                binding.alarmLayout.setVisibility(View.VISIBLE);
                Log.d(TAG, "empty: " + empty);
                binding.spinKit.setVisibility(View.INVISIBLE);
            } else {
                binding.a.setVisibility(View.VISIBLE);
                binding.alarmLayout.setVisibility(View.INVISIBLE);
                Log.d(TAG, "empty: " + empty);
                binding.spinKit.setVisibility(View.INVISIBLE);


            }

        };
        h.postDelayed(r, 500);


        binding.buttonPermisions.setOnClickListener(view -> {

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);

        });

        binding.buttonRefresh.setOnClickListener(view -> {
            per = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (per) {

                creatList();
            } else {
                askPermision();
            }

            if (empty) {
                binding.alarm.setText(alarm);
                binding.a.setVisibility(View.INVISIBLE);
                binding.alarmLayout.setVisibility(View.VISIBLE);
                Log.d(TAG, "empty: " + empty);
                binding.spinKit.setVisibility(View.INVISIBLE);
            } else {
                binding.a.setVisibility(View.VISIBLE);
                binding.alarmLayout.setVisibility(View.INVISIBLE);
                Log.d(TAG, "empty: " + empty);
                binding.spinKit.setVisibility(View.INVISIBLE);


            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        downloadViewModel.isPermission.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    creatList();
                } else {
                    askPermision();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("LongLogTag")
    private void askPermision() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        empty = true;
        alarm = "باید مجوز دسترسی به فایل هارا فعال کنید";
        binding.buttonPermisions.setVisibility(View.VISIBLE);
        binding.buttonRefresh.setVisibility(View.VISIBLE);
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


        if (listFile.length == 0) {
            empty = true;
            alarm = "دانلودی وجود ندارد";
            binding.buttonPermisions.setVisibility(View.INVISIBLE);
            binding.buttonRefresh.setVisibility(View.INVISIBLE);
            Log.d(TAG, "5: ");
        } else {
            LinearLayoutManager horizColors = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
            binding.includedSetting.recAll.setLayoutManager(horizColors);
            downloadAdapter = new DownloadAdapter(getActivity(), listFile);
            binding.includedSetting.recAll.setAdapter(downloadAdapter);
            downloadAdapter.notifyDataSetChanged();
            Log.d(TAG, "listFile: -> " + listFile.length);
            Log.d(TAG, "6: ");

            empty = false;
        }


    }


}