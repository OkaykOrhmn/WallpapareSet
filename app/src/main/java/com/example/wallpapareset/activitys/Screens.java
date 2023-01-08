package com.example.wallpapareset.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.wallpapareset.R;
import com.example.wallpapareset.databinding.ActivityScreensBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Screens extends AppCompatActivity {

    private ActivityScreensBinding binding;
    private boolean isDubblePress = false;
    private NavController navController;
    private Window window;


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

}