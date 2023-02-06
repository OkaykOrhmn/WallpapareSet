package com.example.wallpapareset.fragments.search;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wallpapareset.api.ResponceSearch;
import com.example.wallpapareset.api.SearchRepository;
import com.example.wallpapareset.models.responce.Wallpapares;
import com.example.wallpapareset.utils.Connectivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnect = new MutableLiveData<>();
    private ArrayList<Wallpapares> groupOfSearch = new ArrayList<>();
    public int wichPage = 1;
    private final SearchRepository searchRepository = new SearchRepository();
    public MutableLiveData<ArrayList<Wallpapares>> searchMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponceSearch> responceSearch = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccessSearch = new MutableLiveData<>();
    public boolean isAdd = true;





    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.context = application;

    }

    public void getSavedSearch(String search) {
        isLoading.setValue(true);
        if(Connectivity.isConnected(context)) {
            isConnect.setValue(true);
            if (groupOfSearch.size() == 0 ) {
                //call api
                getAllSearch(search,wichPage);
                isLoading.setValue(false);

            } else {
                //return value
                searchMutableLiveData.setValue(groupOfSearch);
                isLoading.setValue(false);


            }

        }else{
            isConnect.setValue(false);
            isLoading.setValue(false);
        }


    }



    public void getAllSearch(String search, int page) {
        //call api
        isLoading.setValue(true);
        if(Connectivity.isConnected(context)){
            isConnect.setValue(true);
            callSearch(search, page);
            isLoading.setValue(false);
        }else{
            isConnect.setValue(false);
            isLoading.setValue(false);


        }



    }

    private void callSearch(String search, int page) {
        Call<ResponceSearch> call = searchRepository.getAllSearch(search, page);
        call.enqueue(new Callback<ResponceSearch>() {
            @Override
            public void onResponse(Call<ResponceSearch> call, Response<ResponceSearch> response) {
                if(response.isSuccessful()){
                    searchMutableLiveData.setValue(response.body().wallpapares);
                    responceSearch.setValue(response.body());
                    groupOfSearch.clear();
                    groupOfSearch.addAll(response.body().wallpapares);
                    if(response.body().has_next){
                        wichPage=response.body().page_next;
                    }
                    isSuccessSearch.setValue(true);

                }else{
                    isSuccessSearch.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponceSearch> call, Throwable t) {
                isSuccessSearch.setValue(false);


            }
        });
    }
}
