package com.example.wallpapareset.models.responce;

import com.google.gson.annotations.SerializedName;

public class Wallpapares {
        @SerializedName("id")
        public int id = 0;

        @SerializedName("url")
        public String url = "";

        public Wallpapares() {
        }

        public Wallpapares(int id, String url) {
                this.id = id;
                this.url = url;
        }
}
