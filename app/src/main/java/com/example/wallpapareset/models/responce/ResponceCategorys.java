package com.example.wallpapareset.models.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponceCategorys {

    @SerializedName("message")
    public String message = "";

    @SerializedName("status")
    public long status = 0;

    @SerializedName("success")
    public boolean success = false;


    @SerializedName("data")
    public ArrayList<Categorize> categorizes = new ArrayList<>();

}
