package com.example.wallpapareset.fragments.downloads;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class DownloadViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    public MutableLiveData<Boolean> isPermission = new MutableLiveData<>();

    public DownloadViewModel(@NonNull Application application) {
        super(application);
        this.context = application;

    }

    public void permission(Boolean permision){

        if(permision){
            isPermission.setValue(true);
        }else{
            isPermission.setValue(false);
        }
    }


}
