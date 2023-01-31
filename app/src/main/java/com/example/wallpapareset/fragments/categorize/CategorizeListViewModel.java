package com.example.wallpapareset.fragments.categorize;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wallpapareset.models.responce.ResponceCategorysList;
import com.example.wallpapareset.models.responce.Wallpapares;
import com.example.wallpapareset.repositorys.CategorysListRepository;
import com.example.wallpapareset.utils.Connectivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorizeListViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private final CategorysListRepository imagesListRepository = new CategorysListRepository();
    public MutableLiveData<ArrayList<Wallpapares>> wallpaparesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnect = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();




    public CategorizeListViewModel(@NonNull Application application) {
        super(application);
        this.context = application;

    }


    public void getList(String cat){
        isLoading.setValue(true);
        if (Connectivity.isConnected(context)){
            isConnect.setValue(true);
            Call<ResponceCategorysList> call = imagesListRepository.getAllCategorys(cat);
            call.enqueue(new Callback<ResponceCategorysList>() {
                @Override
                public void onResponse(Call<ResponceCategorysList> call, Response<ResponceCategorysList> response) {
                    if(response.isSuccessful()){
                        wallpaparesMutableLiveData.setValue(response.body().wallpapares);
                        isLoading.setValue(false);
                        isSuccess.setValue(true);
                    }else{
                        isSuccess.setValue(false);
                        isLoading.setValue(false);


                    }


                }

                @Override
                public void onFailure(Call<ResponceCategorysList> call, Throwable t) {
                    isLoading.setValue(false);
                    isSuccess.setValue(false);


                }
            });


        }else{
            isLoading.setValue(false);
            isConnect.setValue(false);
            isSuccess.setValue(false);



        }
    }

}
