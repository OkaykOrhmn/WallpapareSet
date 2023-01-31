package com.example.wallpapareset.fragments.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wallpapareset.models.responce.Categorize;
import com.example.wallpapareset.models.responce.ResponceCategorys;
import com.example.wallpapareset.models.responce.ResponceImagesList;
import com.example.wallpapareset.models.responce.Wallpapares;
import com.example.wallpapareset.repositorys.CategorysRepository;
import com.example.wallpapareset.repositorys.ImagesListRepository;
import com.example.wallpapareset.utils.Connectivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListsViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final ImagesListRepository imagesListRepository = new ImagesListRepository();
    private final CategorysRepository categorysListRepository = new CategorysRepository();
    public MutableLiveData<ResponceImagesList> arrayListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Wallpapares>> wallpaparesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Categorize>> categorizeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnect = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> isEmpty = new MutableLiveData<>();
    private ArrayList<Wallpapares> groupOfWall = new ArrayList<>();
    private ArrayList<Categorize> groupOfCat = new ArrayList<>();


    public ListsViewModel(@NonNull Application application) {
        super(application);
        this.context = application;

    }

    public void getHome() {

        if (groupOfWall.size() == 0 && groupOfCat.size() == 0) {
            //call api
            isLoading.setValue(true);
            if(Connectivity.isConnected(context)){
                isConnect.setValue(true);
                callAllCategorys();
                callAllImages();
            }else{
                isConnect.setValue(false);
                isSuccess.setValue(false);

            }

            isLoading.setValue(false);

        } else {
            //return value
            isLoading.setValue(false);
            wallpaparesMutableLiveData.setValue(groupOfWall);
            categorizeMutableLiveData.setValue(groupOfCat);

        }


    }

    public void getAll() {
        //call api
        isLoading.setValue(true);
        if(Connectivity.isConnected(context)){
            isConnect.setValue(true);
            callAllCategorys();
            callAllImages();
        }else{
            isConnect.setValue(false);
            isSuccess.setValue(false);

        }

        isLoading.setValue(false);


    }


    private void callAllImages() {

            Call<ResponceImagesList> call = imagesListRepository.getAllImages(1);
            call.enqueue(new Callback<ResponceImagesList>() {
                @Override
                public void onResponse(Call<ResponceImagesList> call, Response<ResponceImagesList> response) {
                    if(response.isSuccessful()){
                        arrayListMutableLiveData.setValue(response.body());
                        wallpaparesMutableLiveData.setValue(response.body().wallpapares);
                        groupOfWall.clear();
                        groupOfWall.addAll(response.body().wallpapares);
                        isSuccess.setValue(true);
                    }else{
                        isSuccess.setValue(false);


                    }

//                    if(response.body().wallpapares.isEmpty()){
//                        isEmpty.setValue(true);
//                    }else{
//                        isEmpty.setValue(true);
//                    }


                }

                @Override
                public void onFailure(Call<ResponceImagesList> call, Throwable t) {
                    isSuccess.setValue(false);


                }
            });

    }

    private void callAllCategorys() {
            Call<ResponceCategorys> call = categorysListRepository.getAllCategorys();
            call.enqueue(new Callback<ResponceCategorys>() {
                @Override
                public void onResponse(Call<ResponceCategorys> call, Response<ResponceCategorys> response) {
                    if (response.isSuccessful()){
                        categorizeMutableLiveData.setValue(response.body().categorizes);
                        groupOfCat.clear();
                        groupOfCat.addAll(response.body().categorizes);
                        isSuccess.setValue(true);
                    }else{

                        isSuccess.setValue(false);
                    }



                }

                @Override
                public void onFailure(Call<ResponceCategorys> call, Throwable t) {
                    isSuccess.setValue(false);


                }
            });

    }


}
