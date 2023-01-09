package com.example.wallpapareset.fragments.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpapareset.R;

import java.util.ArrayList;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.MyView>{

    private Context context;
    private ArrayList<Integer> suggestions;
    private String title;

    public AllAdapter(ArrayList<Integer> suggestions, String title){
        this.suggestions=suggestions;
        this.title=title;
    }

    @NonNull
    @Override
    public AllAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.all, parent, false);
        return new AllAdapter.MyView(itemview);    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull AllAdapter.MyView holder, int position) {
        holder.icon.setImageResource(suggestions.get(position));
        if (position == 0){
            holder.name.setText(title);
            holder.icon.setClickable(false);
        }else{
            holder.icon.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("photo", suggestions.get(position));
                bundle.putInt("id", position);
                Navigation.findNavController(view).navigate(R.id.action_to_photoPageFragment, bundle);
            });
        }



    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView name;
        public LinearLayout cart;

        public MyView(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.photo_all);
            name = itemView.findViewById(R.id.name);



        }
    }

}