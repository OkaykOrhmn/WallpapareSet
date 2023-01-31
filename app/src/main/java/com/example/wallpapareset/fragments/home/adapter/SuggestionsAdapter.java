package com.example.wallpapareset.fragments.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.example.wallpapareset.models.responce.Categorize;

import java.util.ArrayList;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.MyView>{

    private static final String TAG = "Kia----SuggAdapter----> ";
    private Context context;
    private ArrayList<Categorize> suggestions;

    public SuggestionsAdapter(ArrayList<Categorize> suggestions){
        this.suggestions=suggestions;
    }

    @NonNull
    @Override
    public SuggestionsAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestions_layout, parent, false);
        return new SuggestionsAdapter.MyView(itemview);    }

    @SuppressLint({"UseCompatLoadingForDrawables", "LongLogTag"})
    @Override
    public void onBindViewHolder(@NonNull SuggestionsAdapter.MyView holder, int position) {
        Categorize item = suggestions.get(position);
        holder.name.setText(item.title);
        holder.icon.setImageResource(R.drawable.icons_electronics);
        Log.d(TAG, "onBindViewHolder: "+ item.title);
        holder.icon.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", item.title);

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
        public LinearLayout cart;

        public MyView(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image_sugg);
            name = itemView.findViewById(R.id.name_sugg);
            cart = itemView.findViewById(R.id.linear_sugg);



        }
    }

}