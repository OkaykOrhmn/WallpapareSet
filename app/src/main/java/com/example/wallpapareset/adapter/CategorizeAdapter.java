package com.example.wallpapareset.adapter;

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

public class CategorizeAdapter extends RecyclerView.Adapter<CategorizeAdapter.MyView> {

    private Context context;
    private ArrayList<Suggestions> suggestions;

    public CategorizeAdapter(ArrayList<Suggestions> suggestions) {
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public CategorizeAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorize_layout, parent, false);
        return new CategorizeAdapter.MyView(itemview);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull CategorizeAdapter.MyView holder, int position) {
        holder.icon.setImageResource(suggestions.get(position).icon);
        holder.name.setText(suggestions.get(position).name);
        holder.icon.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", suggestions.get(position).name);

            Navigation.findNavController(view).navigate(R.id.action_to_listFragment, bundle);
        });
//        holder.cart.setBackgroundResource(suggestions.get(position).backGround);

    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView name;

        public MyView(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.photo_all);
            name = itemView.findViewById(R.id.name);



        }
    }
}
