package com.example.wallpapareset.fragments.home.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpapareset.R;
import com.example.wallpapareset.models.responce.Wallpapares;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.MyView>{

    private static final String TAG = "Kia----AllAdapter----> ";
    private Context context;
    private ArrayList<Wallpapares> suggestions;

    public AllAdapter(ArrayList<Wallpapares> suggestions){
        this.suggestions=suggestions;
    }

    @NonNull
    @Override
    public AllAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.all, parent, false);
        return new AllAdapter.MyView(itemview);    }

    @Override
    public void onBindViewHolder(@NonNull AllAdapter.MyView holder, int position) {
        Wallpapares item = suggestions.get(position);
        if (position == 0){
            holder.name.setText(item.url);
            holder.icon.setClickable(false);
            float scale = context.getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (36*scale + 0.5f);
            holder.cart.setPadding(0,dpAsPixels,0,dpAsPixels);
            holder.cart.requestLayout();
        } else{
                Picasso.get().load(item.url).into(holder.icon);
            holder.icon.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.id);
                bundle.putString("url", item.url);
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
        public RelativeLayout cart;

        public MyView(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.photo_all);
            name = itemView.findViewById(R.id.name);
            cart = itemView.findViewById(R.id.all_photo_main);




        }
    }

}