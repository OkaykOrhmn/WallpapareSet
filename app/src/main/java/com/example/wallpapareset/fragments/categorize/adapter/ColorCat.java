package com.example.wallpapareset.fragments.categorize.adapter;

public class ColorCat {
    private int color = 0;
    private String name = "";

    public ColorCat(int color, String name) {
        this.color = color;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
