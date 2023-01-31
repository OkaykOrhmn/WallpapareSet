package com.example.wallpapareset.repositorys;

import com.example.wallpapareset.models.responce.ResponceCategorys;
import com.example.wallpapareset.utils.Tools;

import retrofit2.Call;

public class CategorysRepository {
    public Call<ResponceCategorys> getAllCategorys(){
        //callApi
        Call<ResponceCategorys> call = Tools.getApiServicesInstance().getCategorys();

        return call;
    }
}
