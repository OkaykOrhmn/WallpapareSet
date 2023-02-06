package com.example.wallpapareset.api;


import com.example.wallpapareset.models.responce.ResponceCategorys;
import com.example.wallpapareset.models.responce.ResponceCategorysList;
import com.example.wallpapareset.models.responce.ResponceImagesList;
import com.example.wallpapareset.models.responce.ResponsePhotoPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

    //https://python.aks14.com/api/v1/image-list
    @GET("/api/v1/image-list")
    Call<ResponceImagesList> getImages(@Query("page") int page);

    //https://python.aks14.com/api/v1/category-list
    @GET("/api/v1/category-list")
    Call<ResponceCategorys> getCategorys();

    //https://python.aks14.com/api/v1/image-detail/id
    @GET("/api/v1/image-detail/{id}")
    Call<ResponsePhotoPage> getPhoto(@Path("id") int id);

    //https://python.aks14.com/api/v1/image-list-by-category
    @GET("/api/v1/image-list-by-category")
    Call<ResponceCategorysList> getCategorysList(@Query("category") String category, @Query("page") int page);

    //https://python.aks14.com/api/v1/image-search
    @GET("/api/v1/image-search")
    Call<ResponceSearch> getSearch(@Query("search") String search, @Query("page") int page);



}
