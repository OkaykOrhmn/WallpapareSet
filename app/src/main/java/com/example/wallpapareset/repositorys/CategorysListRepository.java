package com.example.wallpapareset.repositorys;

import com.example.wallpapareset.models.responce.ResponceCategorysList;
import com.example.wallpapareset.utils.Tools;

import retrofit2.Call;


    public class CategorysListRepository {
        public Call<ResponceCategorysList> getAllCategorys(String cat) {
            //callApi
            Call<ResponceCategorysList> call = Tools.getApiServicesInstance().getCategorysList(cat);

            return call;
        }
    }
