package com.example.wallpapareset.repositorys;

import com.example.wallpapareset.models.responce.ResponceImagesList;
import com.example.wallpapareset.utils.Tools;

import retrofit2.Call;

public class ImagesListRepository {

    public Call<ResponceImagesList> getAllImages(int page){
        //callApi
        Call<ResponceImagesList> call = Tools.getApiServicesInstance().getImages(page);

        return call;
    }
}
