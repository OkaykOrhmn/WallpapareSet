package com.example.wallpapareset.api;

import com.example.wallpapareset.models.responce.Wallpapares;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponceSearch {

    @SerializedName("count")
    public int count = 0;

    @SerializedName("has_next")
    public boolean has_next = false;

    @SerializedName("page_next")
    public int page_next = 0;

    @SerializedName("has_previous")
    public boolean has_previous = false;

    @SerializedName("page_previous")
    public String page_previous = "";

    @SerializedName("data")
    public ArrayList<Wallpapares> wallpapares = new ArrayList<>();
}
