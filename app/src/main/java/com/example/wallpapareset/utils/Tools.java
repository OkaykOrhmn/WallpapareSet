package com.example.wallpapareset.utils;

import com.example.wallpapareset.api.ApiClient;
import com.example.wallpapareset.api.ApiServices;

public class Tools {

    private static ApiServices apiServices = null ;

    public static ApiServices getApiServicesInstance (){

        if (apiServices == null ){
            //client
            apiServices = ApiClient.getClient().create(ApiServices.class);
        }

        return apiServices ;
    }
}
