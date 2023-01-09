package com.example.wallpapareset.fragments.settingFragment.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpapareset.R;

import java.io.File;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyView>{

    private Activity activity;
    private Context context;
    private File[] files;

    public DownloadAdapter(Activity activity, File[] files){
        this.files=files;
        this.activity=activity;
    }

    @NonNull
    @Override
    public DownloadAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_downloads, parent, false);
        return new DownloadAdapter.MyView(itemview);    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.MyView holder, int position) {
        File item = files[position];
        holder.name.setText(item.getName());
        Bitmap bmp = BitmapFactory.decodeFile(item.getPath());
        holder.icon.setImageBitmap(bmp);

        holder.selected.setOnClickListener(view -> {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                Uri uri = Uri.parse(item.getPath());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                activity.startActivity(intent);

            } else {
                Toast.makeText(activity, "no", Toast.LENGTH_SHORT).show();
            }

        });





    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public class MyView extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView name;
        public RelativeLayout selected;


        public MyView(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.main_image);
            name = itemView.findViewById(R.id.name_image);
            selected = itemView.findViewById(R.id.selected_image);



        }
    }

}