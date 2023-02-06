package com.example.wallpapareset.api;

import com.example.wallpapareset.models.responce.ResponceCategorysList;
import com.example.wallpapareset.utils.Tools;

import retrofit2.Call;

public class SearchRepository {

    public Call<ResponceSearch> getAllSearch(String search, int page) {
        //callApi
        Call<ResponceSearch> call = Tools.getApiServicesInstance().getSearch(search, page);

        return call;
    }
}
