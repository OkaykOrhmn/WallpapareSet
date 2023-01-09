package com.example.wallpapareset.fragments.categorize.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpapareset.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyView>{

    private Context context;
    private ArrayList<Integer> colors;

    public ColorAdapter(ArrayList<Integer> colors){
        this.colors = colors;
    }

    @NonNull
    @Override
    public ColorAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.colors_row, parent, false);
        return new ColorAdapter.MyView(itemview);    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.MyView holder, int position) {
        holder.color.setBackgroundResource(colors.get(position));
        holder.color.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("back",colors.get(position));

            Navigation.findNavController(view).navigate(R.id.action_to_listFragment, bundle);
        });

//        holder.color.setBackgroundResource(colors.get(position));

    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public ImageView color;

        public MyView(@NonNull View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.main_color);



        }
    }

}