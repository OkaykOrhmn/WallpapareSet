package com.example.wallpapareset.models.responce;

import com.google.gson.annotations.SerializedName;

public class Categorize {

    @SerializedName("id")
    public int id = 0;

    @SerializedName("title")
    public String title = "";

    public Categorize(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
