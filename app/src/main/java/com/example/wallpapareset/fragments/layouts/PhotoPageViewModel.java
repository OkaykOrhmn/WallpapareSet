package com.example.wallpapareset.fragments.layouts;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wallpapareset.models.responce.ResponsePhotoPage;
import com.example.wallpapareset.repositorys.PhotoRepository;
import com.example.wallpapareset.utils.Connectivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoPageViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final PhotoRepository photoRepository = new PhotoRepository();
    public MutableLiveData<String> namePhoto = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnect = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    public PhotoPageViewModel(@NonNull Application application) {
        super(application);
        this.context = application;

    }

    public void getImage(int id){
        isLoading.setValue(true);
        if (Connectivity.isConnected(context)){
            isConnect.setValue(true);
            Call<ResponsePhotoPage> call = photoRepository.getMainPhoto(id);
            call.enqueue(new Callback<ResponsePhotoPage>() {
                @Override
                public void onResponse(Call<ResponsePhotoPage> call, Response<ResponsePhotoPage> response) {
                    if (response.isSuccessful()) {
                        namePhoto.setValue(response.body().wallpapares.url);
                        isLoading.setValue(false);
                        isSuccess.setValue(true);
                    }else{
                        isLoading.setValue(false);
                        isSuccess.setValue(false);
                    }

                }

                @Override
                public void onFailure(Call<ResponsePhotoPage> call, Throwable t) {
                    isLoading.setValue(false);


                }
            });

        }else{
            isLoading.setValue(false);
            isConnect.setValue(false);
            isSuccess.setValue(false);



        }
    }


}
