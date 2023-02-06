package com.example.wallpapareset.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.wallpapareset.R;
import com.example.wallpapareset.databinding.ActivityScreensBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Screens extends AppCompatActivity {

    private static final String TAG = "Kia--MainActivity----> ";
    private ActivityScreensBinding binding;
    private boolean isDubblePress = false;
    private NavController navController;
    private Window window;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreensBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        window = getWindow();



        BottomNavigationView navView = findViewById(R.id.bottomNav);
        navView.setSelectedItemId(R.id.homeFragment);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);


        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {

            switch (navDestination.getId()) {
                case R.id.photoPageFragment:
                    binding.bottomNav.setVisibility(View.INVISIBLE);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainBlack));
                    break;

                case R.id.listFragment:
                    binding.bottomNav.setVisibility(View.INVISIBLE);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainGray));
                    break;

                case R.id.searchFragment:
                    binding.bottomNav.setVisibility(View.INVISIBLE);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainGray));
                    break;


                default:
                    binding.bottomNav.setVisibility(View.VISIBLE);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainGray));
                    break;


            }

        });
    }

    @Override
    public void onBackPressed() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);


        if (navController.getCurrentDestination().getId() == R.id.homeFragment) {
            if (isDubblePress) {

                finish();

            } else {
                this.isDubblePress = true;
                Toast.makeText(this, "برای خروج دوباره امتحان کنید", Toast.LENGTH_LONG).show();
                Handler h = new Handler();
                Runnable r = () -> {
                    isDubblePress = false;
                };
                h.postDelayed(r, 4000);
            }
        } else {
            super.onBackPressed();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        Log.d(TAG, "onRequestPermissionsResult: 1");
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    } else if (Manifest.permission.WRITE_CONTACTS.equals(permission)) {
                        Log.d(TAG, "onRequestPermissionsResult: 2");

                    }
                }
            }
        }
    }





}