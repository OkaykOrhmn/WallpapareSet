package com.example.wallpapareset.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wallpapareset.R;
import com.example.wallpapareset.view.Animations;

public class SplahActivity extends AppCompatActivity {

    private Window window ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        window = getWindow();

        ImageView imageView = findViewById(R.id.icon_main);
        TextView textView = findViewById(R.id.name_main);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mainBlack));

        Handler h = new Handler();
        Runnable r = () -> {
                Intent intent = new Intent(SplahActivity.this, Screens.class);
                startActivity(intent);
                finish();
        };h.postDelayed(r, 3000);
    }
}