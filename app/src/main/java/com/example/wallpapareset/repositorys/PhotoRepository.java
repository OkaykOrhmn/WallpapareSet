package com.example.wallpapareset.repositorys;

import com.example.wallpapareset.models.responce.ResponsePhotoPage;
import com.example.wallpapareset.utils.Tools;

import retrofit2.Call;

public class PhotoRepository {
    public Call<ResponsePhotoPage> getMainPhoto(int id){
        //callApi
        Call<ResponsePhotoPage> call = Tools.getApiServicesInstance().getPhoto(id);

        return call;
    }
}
