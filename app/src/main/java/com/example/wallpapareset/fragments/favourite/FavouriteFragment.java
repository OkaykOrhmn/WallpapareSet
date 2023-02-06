package com.example.wallpapareset.fragments.favourite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallpapareset.models.responce.Wallpapares;
import com.example.wallpapareset.fragments.home.adapter.AllAdapter;
import com.example.wallpapareset.database.SqlFav;
import com.example.wallpapareset.databinding.FragmentFavouriteBinding;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    private static final String TAG = "Kia--FavouriteFr---- > ";
    private FragmentFavouriteBinding binding;
    private AllAdapter allAdapter;
    private ArrayList<Wallpapares> photoArrayList = new ArrayList<>();
    private SqlFav sqlFav;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        sqlFav = new SqlFav(getContext());
        Wallpapares a = new Wallpapares(0, "موردعلاقه ها");
        photoArrayList.clear();
        photoArrayList.add(a);
        photoArrayList.addAll(sqlFav.getData());

        binding.includedFav.progressHorizontal.setVisibility(View.GONE);


        if (photoArrayList.size() == 1 ) {
            binding.emptyText.setVisibility(View.VISIBLE);
            binding.scrollFav.setVisibility(View.INVISIBLE);

        } else {
            binding.emptyText.setVisibility(View.INVISIBLE);
            binding.scrollFav.setVisibility(View.VISIBLE);
            int numberOfColumns = 2;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
            staggeredGridLayoutManager.setReverseLayout(false);
            binding.includedFav.recAll.setLayoutManager(staggeredGridLayoutManager);
            allAdapter = new AllAdapter(photoArrayList);
            binding.includedFav.recAll.setAdapter(allAdapter);
            allAdapter.notifyDataSetChanged();
        }

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}