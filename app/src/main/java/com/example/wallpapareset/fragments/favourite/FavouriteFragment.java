package com.example.wallpapareset.fragments.favourite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallpapareset.R;
import com.example.wallpapareset.models.model.Photo;
import com.example.wallpapareset.database.SqlFav;
import com.example.wallpapareset.databinding.FragmentFavouriteBinding;
import com.example.wallpapareset.fragments.favourite.adapter.FavAdapter;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    private static final String TAG = "Kia--FavouriteFr---- > ";
    private FragmentFavouriteBinding binding;
    private FavAdapter favAdapter;
    private ArrayList<Photo> photoArrayList;
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

        sqlFav.Insert(0, R.color.white);

        photoArrayList = sqlFav.getData();
        Log.d(TAG, "size list is -> : " + photoArrayList.size());


        if (photoArrayList.size() == 1) {
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
            favAdapter = new FavAdapter(photoArrayList, "مورد علاقه ها");
            binding.includedFav.recAll.setAdapter(favAdapter);
            favAdapter.notifyDataSetChanged();
        }

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}