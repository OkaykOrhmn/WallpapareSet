package com.example.wallpapareset.models.responce;

import com.google.gson.annotations.SerializedName;

public class ResponsePhotoPage {
    @SerializedName("message")
    public String message = "";

    @SerializedName("status")
    public long status = 0;

    @SerializedName("success")
    public boolean success = false;


    @SerializedName("data")
    public Wallpapares wallpapares = new Wallpapares();
}
