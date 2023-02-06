package com.example.wallpapareset.fragments.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wallpapareset.api.ResponceSearch;
import com.example.wallpapareset.api.SearchRepository;
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

    public MutableLiveData<ResponceImagesList> responceImageList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Wallpapares>> wallpaparesMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Categorize>> categorizeMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoadingSearch = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnect = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccessList = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccessCat = new MutableLiveData<>();

    public boolean isAdd = true;
    public int wichPage = 1;
    public int wichPageSearch = 1;

    public ArrayList<Wallpapares> groupOfWall = new ArrayList<>();
    private ArrayList<Categorize> groupOfCat = new ArrayList<>();


    public ListsViewModel(@NonNull Application application) {
        super(application);
        this.context = application;

    }


    public void getSavedData() {
        isLoading.setValue(true);
        if(Connectivity.isConnected(context)) {
            isConnect.setValue(true);
            if (groupOfWall.size() == 0 || groupOfCat.size() == 0) {
                //call api
                getAll(wichPage);
                isLoading.setValue(false);

            } else {
                //return value
                wallpaparesMutableLiveData.setValue(groupOfWall);
                categorizeMutableLiveData.setValue(groupOfCat);
                isLoading.setValue(false);


            }

        }else{
            isConnect.setValue(false);
            isLoading.setValue(false);
        }


    }

    public void getAll(int page) {
        //call api
        isLoading.setValue(true);
        if(Connectivity.isConnected(context)){
            isConnect.setValue(true);
            callAllCategorys();
            callAllImages(page);
            isLoading.setValue(false);
        }else{
            isConnect.setValue(false);
            isLoading.setValue(false);


        }



    }

    private void callAllImages(int page) {

            Call<ResponceImagesList> call = imagesListRepository.getAllImages(page);
            call.enqueue(new Callback<ResponceImagesList>() {
                @Override
                public void onResponse(Call<ResponceImagesList> call, Response<ResponceImagesList> response) {
                    if(response.isSuccessful()){
                        responceImageList.setValue(response.body());
                        wallpaparesMutableLiveData.setValue(response.body().wallpapares);
                        groupOfWall.clear();
                        groupOfWall.addAll(response.body().wallpapares);
                        isSuccessList.setValue(true);
                        if(response.body().has_next){
                            wichPage=response.body().page_next;
                        }
                    }else{
                        isSuccessList.setValue(false);


                    }


                }

                @Override
                public void onFailure(Call<ResponceImagesList> call, Throwable t) {
                    isSuccessList.setValue(false);


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
                        isSuccessCat.setValue(true);
                    }else{

                        isSuccessCat.setValue(false);
                    }



                }

                @Override
                public void onFailure(Call<ResponceCategorys> call, Throwable t) {
                    isSuccessCat.setValue(false);


                }
            });

    }






}
